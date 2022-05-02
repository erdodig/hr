package hu.webuni.hr.dodi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.dodi.model.HolidayRequest;
import hu.webuni.hr.dodi.model.HolidayRequestState;
import hu.webuni.hr.dodi.model.HolidayRequest_;

public class HolidayRequestSpecifications {

	public static Specification<HolidayRequest> hasState(HolidayRequestState state) {
		return (root, cq, cb) -> cb.equal(root.get(HolidayRequest_.holidayRequestState), state);
	}

	public static Specification<HolidayRequest> hasRequestName(String requestName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(HolidayRequest_.requestingEmployee).get("name"))
				, requestName.toLowerCase() + "%");
	}

	public static Specification<HolidayRequest> hasLeaderName(String leaderName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(HolidayRequest_.leader).get("name"))
				, leaderName.toLowerCase() + "%");
	}

	public static Specification<HolidayRequest> betweenRequestTime(LocalDateTime fromRequestTime,
			LocalDateTime toRequestTime) {
		return (root, cq, cb) -> cb.between(root.get(HolidayRequest_.requestTime)
				, fromRequestTime, toRequestTime);
	}

	public static Specification<HolidayRequest> hasHolidayDate(LocalDate fromDate, LocalDate toDate) {
		return (root, cq, cb) -> cb.and(
				cb.or(
						cb.between(root.get(HolidayRequest_.fromDate), fromDate, toDate),
						cb.between(root.get(HolidayRequest_.toDate), fromDate, toDate)
						)
				);
	}

}
