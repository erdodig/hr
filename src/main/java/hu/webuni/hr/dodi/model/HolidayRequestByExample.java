package hu.webuni.hr.dodi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HolidayRequestByExample {

	private LocalDate fromDate;
	
	private LocalDate toDate;
	
	private LocalDateTime fromRequestTime;
	
	private LocalDateTime toRequestTime;
	
	private HolidayRequestState holidayRequestState;
	
	private String requestName;
	
	private String leaderName;
	
	private String sorting;
	
	private String direction;
	
	public HolidayRequestByExample() {
	}

	public HolidayRequestByExample(LocalDate fromDate, LocalDate toDate, LocalDateTime fromRequestTime,
			LocalDateTime toRequestTime, HolidayRequestState holidayRequestState, String requestName,
			String leaderName, String sorting, String direction) {
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.fromRequestTime = fromRequestTime;
		this.toRequestTime = toRequestTime;
		this.holidayRequestState = holidayRequestState;
		this.requestName = requestName;
		this.leaderName = leaderName;
		this.sorting = sorting;
		this.direction = direction;
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

	public LocalDateTime getFromRequestTime() {
		return fromRequestTime;
	}

	public void setFromRequestTime(LocalDateTime fromRequestTime) {
		this.fromRequestTime = fromRequestTime;
	}

	public LocalDateTime getToRequestTime() {
		return toRequestTime;
	}

	public void setToRequestTime(LocalDateTime toRequestTime) {
		this.toRequestTime = toRequestTime;
	}

	public HolidayRequestState getHolidayRequestState() {
		return holidayRequestState;
	}

	public void setHolidayRequestState(HolidayRequestState holidayRequestState) {
		this.holidayRequestState = holidayRequestState;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getSorting() {
		return sorting;
	}

	public void setSorting(String sorting) {
		this.sorting = sorting;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
}
