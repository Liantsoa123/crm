package site.easy.to.build.crm.service.system;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.repository.SystemRepository;

@Service
public class SystemServiceImpl implements SystemService {

    private final SystemRepository systemRepository;

    @Autowired
    public SystemServiceImpl(SystemRepository systemRepository) {
        this.systemRepository = systemRepository;
    }

    @Override
    @Transactional
    public void resetAllData() {
        systemRepository.resetData();
    }
}