package com.files.v1.files.service;

import com.files.v1.files.domain.FilesToDomainMapper;

import java.util.List;

public interface FilesToDomainMapperService {

    public FilesToDomainMapper save(FilesToDomainMapper filesToDomainMapper);

    public FilesToDomainMapper getByDomainId(String domainId);

    public List<FilesToDomainMapper> getByDomainIds(List<String> domainIds);

    public FilesToDomainMapper addFileId(String domainId, String fileId);

    public void deleteByDomainId(String domainId);

    public void removeFileByFileId(String domainId, String fileId);
}
