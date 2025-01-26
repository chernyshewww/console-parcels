package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.exception.TruckFileReadException;
import com.hofftech.deliverysystem.model.Truck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TruckRepositoryTest {

    @InjectMocks
    private TruckRepository truckRepository;

}
