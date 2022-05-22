package com.cloud.web.domain;

import com.cloud.web.domain.enums.AttachmentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 전체적으로 board form에서 들어온 모든 이미지 혹은 일반 파일을
 * 확장자에 따라서 새롭게 객체를 만들어주고
 *
 * 모든 이미지와 일반 파일을 하나의 공통된 list로 관리하기 위함이다.
 */
@Component
@PropertySource("classpath:img.properties")
public class FileStore {


    //private String fileDirPath = "C:\\WebCloud\\web\\src\\main\\resources\\static\\files";


    String fileDirPath;

    public FileStore(@Value(value = "${img.path}") String path){
        this.fileDirPath = System.getProperty("user.dir") + path;
    }


    /**
     * 매개 변수로 이미지 혹은 일반 파일에 해당 하는 단 1개의 종류의 파일 list가 들어온다.
     *
     * MultipartFile Type을 Attachment 라는 우리가 정의한 Class Type으로 다시 한번 list로 묶어준다.
     *
     * 주된 기능은 Class Type을 바꿔서 새롭게 저장 ( 1개 종류의 list에 대해서 저장 )
     */
    public List<Attachment> storeFiles(List<MultipartFile> multipartFiles, AttachmentType attachmentType) throws IOException {
        List<Attachment> attachments = new ArrayList<>();

        // Attachment 가 올 수 있는 list에 각종 이미지 파일과 , 일반 파일이 함께 저장이 된다. (다수일 경우)
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                attachments.add(storeFile(multipartFile, attachmentType));
            }
        }

        return attachments;
    }

    /**
     * 파일 저장 로직
     * 하나의 파일 이름 , 저장 경로 , 파일 확장자를 포함한 객체를 반환
     */
    public Attachment storeFile(MultipartFile multipartFile, AttachmentType attachmentType) throws IOException {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename); // 원래 이름을 제거하고 ( UUID로 만든 string과 확장자를 붙인다. )

        multipartFile.transferTo(new File(createPath(storeFilename, attachmentType))); // filepath 경로 안에 저장을 하고 , 파일 확장자에 따라서 files 폴더 아래 다른 폴더로 저장된다.

        return Attachment.builder()
                .originFileName(originalFilename)
                .storePath(storeFilename)
                .attachmentType(attachmentType)
                .build();

    }

    /**
     * 파일 경로 구성
     * 파일을 저장하는데 있어 이미지 파일을 저장할 경로와 일반적인 파일을 저장할 경로를 구분하기 위해
     * AttachmentType을 파라미터로 입력받아 파일의 경로를 설정해주었다
     * @param storeFilename
     * @param attachmentType
     * @return
     */
    public String createPath(String storeFilename, AttachmentType attachmentType) {
        String viaPath = (attachmentType == AttachmentType.IMAGE) ? "images/" : "generals/";
        return fileDirPath+viaPath+storeFilename;
    }

    /**
     * 저장할 파일 이름 구성 ( 이름이 중복 가능성이 있음으로 UUID로 랜덤 string으로 받고 확장자를 넣어준다.
     * 그것이 곧 저장할 파일 이름
     * @param originalFilename
     * @return
     */
    private String createStoreFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        String storeFilename = uuid + ext;

        return storeFilename;
    }

    /**
     * 확장자 추출 (확장자만 추출한다)
     * @param originalFilename
     * @return
     */
    private String extractExt(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(idx);
        return ext;
    }


}
