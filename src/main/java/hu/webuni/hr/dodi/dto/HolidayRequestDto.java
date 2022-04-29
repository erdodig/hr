package hu.webuni.hr.dodi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import hu.webuni.hr.dodi.model.HolidayRequestState;

public class HolidayRequestDto {

	private Long id;
	
	@NotNull
	@Future
	private LocalDate fromDate;
	
	@NotNull
	@Future
	private LocalDate toDate;
	
	private LocalDateTime requestTime;
	
	private EmployeeDto requestingEmployee;
	
	private HolidayRequestState holidayRequestState;
	
	private EmployeeDto leader;
	
	public HolidayRequestDto() {
	}

	public HolidayRequestDto(LocalDate fromDate, LocalDate toDate, LocalDateTime requestTime,
			EmployeeDto requestingEmployee) {
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.requestTime = requestTime;
		this.requestingEmployee = requestingEmployee;
		this.holidayRequestState = HolidayRequestState.REQUEST;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}

	public EmployeeDto getRequestingEmployee() {
		return requestingEmployee;
	}

	public void setRequestingEmployee(EmployeeDto requestingEmployee) {
		this.requestingEmployee = requestingEmployee;
	}

	public HolidayRequestState getHolidayRequestState() {
		return holidayRequestState;
	}

	public void setHolidayRequestState(HolidayRequestState holidayRequestState) {
		this.holidayRequestState = holidayRequestState;
	}

	public EmployeeDto getLeader() {
		return leader;
	}

	public void setLeader(EmployeeDto leader) {
		this.leader = leader;
	}
	
}
