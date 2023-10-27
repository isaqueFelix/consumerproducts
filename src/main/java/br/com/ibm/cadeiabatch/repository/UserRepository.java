package br.com.ibm.cadeiabatch.repository;

import br.com.ibm.cadeiabatch.entity.Usuario;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserRepository extends CrudRepository<Usuario, Long> {

}