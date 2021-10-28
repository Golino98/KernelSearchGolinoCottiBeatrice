package com.golino.cotti.classes;
import java.util.List;

public interface KernelBuilder
{
    public Kernel build(List<Item> items, Configuration config);
}