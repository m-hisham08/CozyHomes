package com.hisham.HomeCentre.repositories;

import com.hisham.HomeCentre.models.Category;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void CategoryRepository_ExistsByName_ReturnsTrueOrFalse(){
        //Arrange
        Category category = new Category();
        category.setName("Electronics");
        category.setCreatedAt(new Date().toInstant());
        category.setCreatedBy(Integer.toUnsignedLong(1));
        categoryRepository.save(category);

        //Act
        Boolean isExists = categoryRepository.existsByName("Electronics");

        //Assert
        Assertions.assertThat(isExists).isTrue();
    }

    @Test
    public void CategoryRepository_Save_ReturnsSavedCategory(){
        //Arrange
        Category category1 = new Category();
        category1.setName("Electronics");
        category1.setCreatedAt(new Date().toInstant());
        category1.setCreatedBy(Integer.toUnsignedLong(1));

        Category category2 = new Category();
        category2.setName("");

        Category category3 = new Category();

        //Act AND Assert
        Category savedCategory1 = categoryRepository.save(category1);
        Assertions.assertThat(savedCategory1.getId()).isGreaterThan(0);

        Assertions.assertThatThrownBy(() -> categoryRepository.save(category2)).isInstanceOf(Exception.class);
        Assertions.assertThatThrownBy(() -> categoryRepository.save(category3)).isInstanceOf(Exception.class);
    }
}
