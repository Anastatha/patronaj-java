package org.example.pat.dto.user;

import org.example.pat.entity.consts.Label;

import java.util.List;

public record LabelsInput(List<Label> labels) {
}