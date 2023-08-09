package com.example.world.file;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UploadedFile, Long> {

}
