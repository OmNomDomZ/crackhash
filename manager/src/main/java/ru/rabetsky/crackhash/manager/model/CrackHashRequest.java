package ru.rabetsky.crackhash.manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrackHashRequest {

    private String hash;
    private Integer maxLength;
}
