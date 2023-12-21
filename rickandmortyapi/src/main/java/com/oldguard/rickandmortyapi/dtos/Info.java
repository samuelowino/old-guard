package com.oldguard.rickandmortyapi.dtos;

public record Info(
        int count,
        int pages,
        String next,
        String prev
) {
}
