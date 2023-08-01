package com.ysy.myapp.post;

//import com.ysy.myapp.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select * " + "from post " + "order by no asc", nativeQuery = true)
    List<Post> findAllByOrderByNo();

    Post save(Post post);


    Optional<Object> findByNo(long no);

    void deleteById(long no);

    Optional<Post> findById(long no);
}
