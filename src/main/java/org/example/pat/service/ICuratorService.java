package org.example.pat.service;

import org.example.pat.dto.curator.CreateCuratorInput;
import org.example.pat.dto.curator.UpdateCuratorInput;
import org.example.pat.entity.Curator;

import java.util.List;

public interface ICuratorService {
    Curator createCurator(CreateCuratorInput input);
    Curator getCurator(Long id);
    Curator updateCurator(Long id, UpdateCuratorInput input);
    Curator softDeleteCurator(Long id);
    List<Curator> getAllCurator();
    Curator getCuratorByEmail(String email);
}
