package com.app.ws.microservice.service;

import com.app.ws.microservice.exceptions.UserException;
import com.app.ws.microservice.io.entity.AddressEntity;
import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.io.repository.AddressRepository;
import com.app.ws.microservice.io.repository.UserRepository;
import com.app.ws.microservice.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private AddressRepository addressRepository;

    @Override
    public List<AddressDto> findAddresses(final String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            return Collections.emptyList();
        }

        List<AddressEntity> allByUserDto = addressRepository.findAllByUserDto(userEntity);
        if(CollectionUtils.isEmpty(allByUserDto)) {
            throw new UserException(String.format("Could not find address associated to id %s", userId));
        }
        Type listType = new TypeToken<List<AddressDto>>() {}.getType();
        return modelMapper.map(allByUserDto, listType);

    }

    @Override
    public AddressDto findByAddressId(final String addressId) {
        AddressEntity byAddressId = addressRepository.findByAddressId(addressId);
        if (byAddressId == null) {
            throw new UserException(String.format("Could not find single Address for address id %s", addressId));
        }

        return modelMapper.map(byAddressId, AddressDto.class);
    }

    @Autowired
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setModelMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setAddressRepository(final AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
}
