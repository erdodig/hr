package hu.webuni.hr.dodi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;

//@NamedEntityGraph(
//		name = "employee.position.employees",
//		attributeNodes = {
//				@NamedAttributeNode(value = "requestingEmployee", subgraph = "requesting.position"),
//				@NamedAttributeNode(value = "leader", subgraph = "leader.position"),
//		},
//		subgraphs = {
//				@NamedSubgraph( attributeNodes = { @NamedAttributeNode(value = "position") }, name = "requesting.position"),
//				@NamedSubgraph(attributeNodes = { @NamedAttributeNode(value = "position") }, name = "leader.position")
//		}
//)
@Entity
public class HolidayRequest {

	@Id
	@GeneratedValue
	private Long id;
	
	private LocalDate fromDate;
	
	private LocalDate toDate;
	
	private LocalDateTime requestTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Employee requestingEmployee;
	
	private HolidayRequestState holidayRequestState;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Employee leader;
	
	public HolidayRequest() {
	}

	public HolidayRequest(LocalDate fromDate, LocalDate toDate, LocalDateTime requestTime, Employee requestingEmployee) {
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

	public Employee getRequestingEmployee() {
		return requestingEmployee;
	}

	public void setRequestingEmployee(Employee requestingEmployee) {
		this.requestingEmployee = requestingEmployee;
	}

	public HolidayRequestState getHolidayRequestState() {
		return holidayRequestState;
	}

	public void setHolidayRequestState(HolidayRequestState holidayRequestState) {
		this.holidayRequestState = holidayRequestState;
	}

	public Employee getLeader() {
		return leader;
	}

	public void setLeader(Employee leader) {
		this.leader = leader;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HolidayRequest other = (HolidayRequest) obj;
		return Objects.equals(id, other.id);
	}
	
}
