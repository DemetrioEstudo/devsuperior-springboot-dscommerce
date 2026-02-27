package br.com.klsys.dscommerce.services;

import br.com.klsys.dscommerce.dto.CategoryDTO;
import br.com.klsys.dscommerce.dto.ProductDTO;
import br.com.klsys.dscommerce.entities.Category;
import br.com.klsys.dscommerce.entities.Product;
import br.com.klsys.dscommerce.repositories.CategoryRepository;
import br.com.klsys.dscommerce.repositories.ProductRepository;
import br.com.klsys.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = repository.findAll();
        return result.stream().map(x -> new CategoryDTO(x)).toList();
    }


}
