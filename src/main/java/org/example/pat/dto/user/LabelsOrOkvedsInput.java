package org.example.pat.dto.user;

import jakarta.annotation.Nullable;
import org.example.pat.entity.consts.Label;

import java.util.List;

public record LabelsOrOkvedsInput(
        List<Label> labels,
        List<String> okved
) {
}
