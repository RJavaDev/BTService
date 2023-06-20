package uz.BTService.btservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.BTService.btservice.entity.BannerEntity;
import uz.BTService.btservice.repository.BannerRepository;
import uz.BTService.btservice.service.builder.BaseServiceBuilder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class BannerService implements BaseServiceBuilder<BannerEntity> {

    private final BannerRepository bannerRepository;
    private final AttachService attachService;


    @Override
    public boolean addObject(BannerEntity createObject) {
        Optional<BannerEntity> bannerEntityDB = bannerRepository.findByPosition(createObject.getPosition());
        if (bannerEntityDB.isPresent()) {
            BannerEntity banner = bannerEntityDB.get();
            attachService.deleteById(banner.getAttachId());
            bannerRepository.delete(banner);
            bannerRepository.save(createObject);
            return true;
        }
        bannerRepository.save(createObject);
        return true;
    }

    @Override
    public BannerEntity getObjectById(Integer id) {
        return bannerRepository.getBannerId(id);
    }

    @Override
    public List<BannerEntity> getAllObject() {
        return bannerRepository.getAllBannerEntity();
    }
}
