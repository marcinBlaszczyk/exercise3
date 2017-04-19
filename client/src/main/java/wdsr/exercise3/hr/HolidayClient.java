package wdsr.exercise3.hr;

import java.net.URL;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import wdsr.exercise3.hr.ws.EmployeeType;
import wdsr.exercise3.hr.ws.HolidayRequest;
import wdsr.exercise3.hr.ws.HolidayResponse;
import wdsr.exercise3.hr.ws.HolidayType;
import wdsr.exercise3.hr.ws.HumanResource;
import wdsr.exercise3.hr.ws.HumanResourceService;


// TODO Complete this class to book holidays by issuing a request to Human Resource web service.
// In order to see definition of the Human Resource web service:
// 1. Run HolidayServerApp.
// 2. Go to http://localhost:8090/holidayService/?wsdl
public class HolidayClient {
	/**
	 * Creates this object
	 * @param wsdlLocation URL of the Human Resource web service WSDL
	 */
	private HumanResourceService humanResourceService;
	public HolidayClient(URL wsdlLocation) {
		humanResourceService = new HumanResourceService(wsdlLocation);
	}
	public int bookHoliday(int employeeId, String firstName, String lastName, Date startDate, Date endDate)
			throws ProcessingException {
		HumanResource humanResource = humanResourceService.getHumanResourcePort();
		try {
			HolidayResponse holidayResponse;
			holidayResponse = humanResource.holiday(createHolidayRequest(employeeId, firstName, lastName, startDate, endDate));
			return holidayResponse.getRequestId();
		} catch (Exception e) {
			throw new ProcessingException();
		}
	}
	
	private HolidayType createHoliday(Date startDate, Date endDate)
			throws DatatypeConfigurationException {
		HolidayType holidayType = new HolidayType();
		holidayType.setStartDate(startDate);
		holidayType.setEndDate(endDate);
		return holidayType;
	}
	
	private EmployeeType createHolidayEmployee(int employeeId, String firstName, String lastName) {
		EmployeeType employeeType = new EmployeeType();
		employeeType.setNumber(employeeId);
		employeeType.setFirstName(firstName);
		employeeType.setLastName(lastName);
		return employeeType;
	}
	
	private HolidayRequest createHolidayRequest(int employeeId, String firstName, String lastName, Date startDate,
			 			Date endDate) throws DatatypeConfigurationException {
				 		HolidayRequest holidayRequest = new HolidayRequest();
				 		holidayRequest.setEmployee(createHolidayEmployee(employeeId, firstName, lastName));
				 		holidayRequest.setHoliday(createHoliday(startDate, endDate));
				 		return holidayRequest;
			 	}
	
}
