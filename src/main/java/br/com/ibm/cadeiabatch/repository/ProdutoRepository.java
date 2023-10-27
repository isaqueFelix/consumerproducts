package br.com.ibm.cadeiabatch.repository;

import br.com.ibm.cadeiabatch.entity.Produto;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableScan
public interface ProdutoRepository extends CrudRepository<Produto, UUID> {

    List<Produto> findAll();

    List<Produto> findByDescricaoContains(String description);
}

