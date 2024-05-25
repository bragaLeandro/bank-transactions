package br.com.ibm.service;

import br.com.ibm.dto.PixKeyDto;
import br.com.ibm.entity.PixKey;
import br.com.ibm.repository.PixKeyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class PixKeyService {

    private final PixKeyRepository pixKeyRepository;
    private final ModelMapper modelMapper;

    public PixKeyService(PixKeyRepository pixKeyRepository, ModelMapper modelMapper) {
        this.pixKeyRepository = pixKeyRepository;
        this.modelMapper = modelMapper;
    }

    public PixKey findPixKeyByKeyValue(String keyValue) {
        return pixKeyRepository.findPixKeyByKeyValue(keyValue)
                .orElseThrow(() -> new IllegalArgumentException("Pix Key not found"));
    }

    public PixKey createPixKey(PixKeyDto pixKeyDto) {
        PixKey pixKey = modelMapper.map(pixKeyDto, PixKey.class);
        return pixKeyRepository.save(pixKey);
    }
}
