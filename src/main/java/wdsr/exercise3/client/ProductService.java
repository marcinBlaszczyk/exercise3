package wdsr.exercise3.client;

import java.util.List;
import java.util.Set;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import wdsr.exercise3.model.Product;
import wdsr.exercise3.model.ProductType;

public class ProductService extends RestClientBase {
	private final String PATH = "products";

	protected ProductService(final String serverHost, final int serverPort, final Client client) {
		super(serverHost, serverPort, client);
	}

	/**
	 * Looks up all products of given types known to the server.
	 * 
	 * @param types
	 *            Set of types to be looked up
	 * @return A list of found products - possibly empty, never null.
	 */
	public List<Product> retrieveProducts(Set<ProductType> types) {
		WebTarget wt = baseTarget.path(PATH);
		List<Product> rs = wt.queryParam("type", types.toArray()).request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Product>>() {
				});
		return rs;
	}

	/**
	 * Looks up all products known to the server.
	 * 
	 * @return A list of all products - possibly empty, never null.
	 */
	public List<Product> retrieveAllProducts() {
		WebTarget wt = baseTarget.path(PATH);
		List<Product> rs = wt.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Product>>() {
		});
		return rs;
	}

	/**
	 * Looks up the product for given ID on the server.
	 * 
	 * @param id
	 *            Product ID assigned by the server
	 * @return Product if found
	 * @throws NotFoundException
	 *             if no product found for the given ID.
	 */
	public Product retrieveProduct(int id) {
		WebTarget wt = baseTarget.path(PATH + "/" + String.valueOf(id));
		Response rs = wt.request(MediaType.APPLICATION_JSON).get(Response.class);
		if (rs.getStatus() != Response.Status.OK.getStatusCode()) {
			throw new NotFoundException();
		}
		return rs.readEntity(Product.class);
	}

	/**
	 * Creates a new product on the server.
	 * 
	 * @param product
	 *            Product to be created. Must have null ID field.
	 * @return ID of the new product.
	 * @throws WebApplicationException
	 *             if request to the server failed
	 */
	public int storeNewProduct(Product product) {
		WebTarget wt = baseTarget.path(PATH);
		Response rs = wt.request(MediaType.APPLICATION_JSON).post(Entity.json(product), Response.class);
		if (rs.getStatus() != 201) {
			throw new WebApplicationException();
		}
		return rs.readEntity(Product.class).getId();
	}

	/**
	 * Updates the given product.
	 * 
	 * @param product
	 *            Product with updated values. Its ID must identify an existing
	 *            resource.
	 * @throws NotFoundException
	 *             if no product found for the given ID.
	 */
	public void updateProduct(Product product) {
		WebTarget wt = baseTarget.path(PATH + "/" + String.valueOf(product.getId()));
		Response rs = wt.request(MediaType.APPLICATION_JSON).put(Entity.json(product), Response.class);
		if (rs.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			throw new NotFoundException();
		}
	}

	/**
	 * Deletes the given product.
	 * 
	 * @param product
	 *            Product to be deleted. Its ID must identify an existing
	 *            resource.
	 * @throws NotFoundException
	 *             if no product found for the given ID.
	 */
	public void deleteProduct(Product product) {
		WebTarget wt = baseTarget.path(PATH + "/" + String.valueOf(product.getId()));
		Response rs = wt.request(MediaType.APPLICATION_JSON).delete(Response.class);
		if (rs.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			throw new NotFoundException();
		}
	}
}
