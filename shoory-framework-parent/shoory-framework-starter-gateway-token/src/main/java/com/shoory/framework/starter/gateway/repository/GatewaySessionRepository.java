package com.shoory.framework.starter.gateway.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shoory.framework.starter.gateway.document.GatewaySessions;


@Repository
public interface GatewaySessionRepository extends CrudRepository<GatewaySessions, String> {
	public List<GatewaySessions> findByCredential(String credential);
}
