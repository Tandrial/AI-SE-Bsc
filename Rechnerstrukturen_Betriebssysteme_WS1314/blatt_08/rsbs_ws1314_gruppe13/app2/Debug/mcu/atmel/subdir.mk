################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../mcu/atmel/mcu_atxmega128a1.c 

OBJS += \
./mcu/atmel/mcu_atxmega128a1.o 

C_DEPS += \
./mcu/atmel/mcu_atxmega128a1.d 


# Each subdirectory must supply rules for building sources it contributes
mcu/atmel/%.o: ../mcu/atmel/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: AVR Compiler'
	avr-gcc -Wall -g2 -gstabs -O2 -fpack-struct -fshort-enums -ffunction-sections -fdata-sections -std=gnu99 -funsigned-char -funsigned-bitfields -mmcu=atxmega128a1 -DF_CPU=2000000UL -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -c -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


