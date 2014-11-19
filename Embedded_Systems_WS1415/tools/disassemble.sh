#!/bin/sh
objdump -bbinary -mi386 -D -Mintel,i8086 $1
