# cc: 
#   Compiliert den CPU neu.
#
# ss <?.hex>: 
#   Startet den Simulator, fügt Signale zum Wave-Fenster hinzu und führt 
#   die Simulation aus. Die Simulation läuft anschließend solange, bis
#   an die Adresse FF der Wert 00FF geschrieben wird. Beim Befehlsaufruf
#   muss als parameter die Initialisierungsdatei für das RAM angegeben
#   werden. Beispiel: "ss ../ram/tiny.hex"
#
# rs:
#   Dieser Befehl kann nur bei laufender Simulation ausgeführt werden! 
#   Er compiliert die VHDL-Dateien neu und beginnt die Simulation von 
#   Anfang an neu. Die Simulations endet, wie bei startsim, automatisch.
#   Die RAM-Datei wird dabei ebenfalls neu geladen.    

proc cc {} {
    set filepath "C:/Users/Michael/Dropbox/Studium/FS_3/Rechnerstrukturen und Betriebssysteme/Uebungsblaetter/Loesungen/Blatt 07/"
	append cpu_pack $filepath "src/cpu_pack.vhd"
	append cpu_reg $filepath "src/cpu_reg.vhd"
	append cpu_alu $filepath "src/cpu_alu.vhd"
	append cpu_ctrl $filepath "src/cpu_ctrl.vhd"
	append cpu $filepath "src/cpu.vhd"
	append ram $filepath "src/ram.vhd"
	append tb_cpu $filepath "src/tb_cpu.vhd"
    
	vlib work
	vmap work work	
    vcom -work work -2002 -explicit $cpu_pack $cpu_reg $cpu_alu $cpu_ctrl $cpu $ram $tb_cpu
}

proc ss {ram_init_file} {    
    vsim -voptargs=+acc work.tb_cpu -gINIT_FILE=$ram_init_file
    
  add wave -group Clk {sim:/tb_cpu/cpu0/clk} {sim:/tb_cpu/cpu0/reset}
  add wave -group Memory -radix hexadecimal {sim:/tb_cpu/cpu0/mem_read} {sim:/tb_cpu/cpu0/mem_write} {sim:/tb_cpu/cpu0/mem_addr} {sim:/tb_cpu/cpu0/mem_read_data} {sim:/tb_cpu/cpu0/mem_write_data} {sim:/tb_cpu/memory/ram}
  add wave -group Registers -radix hexadecimal {sim:/tb_cpu/cpu0/reg_d} {sim:/tb_cpu/cpu0/reg_q}
  add wave -group ALU  -radix hexadecimal {sim:/tb_cpu/cpu0/alu_mode} {sim:/tb_cpu/cpu0/alu_op1} {sim:/tb_cpu/cpu0/alu_op2} {sim:/tb_cpu/cpu0/alu_carry_in} {sim:/tb_cpu/cpu0/alu_result} {sim:/tb_cpu/cpu0/alu_carry_out} {sim:/tb_cpu/cpu0/alu_instance/dec}
  add wave -group ALU_temporary -radix hexadecimal {sim:/tb_cpu/cpu0/alu_instance/tmp1a} {sim:/tb_cpu/cpu0/alu_instance/tmp1b} {sim:/tb_cpu/cpu0/alu_instance/tmp1c} {sim:/tb_cpu/cpu0/alu_instance/tmp1d} {sim:/tb_cpu/cpu0/alu_instance/tmp2a} {sim:/tb_cpu/cpu0/alu_instance/tmp2b} {sim:/tb_cpu/cpu0/alu_instance/tmp2c} {sim:/tb_cpu/cpu0/alu_instance/tmpe} {sim:/tb_cpu/cpu0/alu_instance/tmp_r} {sim:/tb_cpu/cpu0/alu_instance/tmp_inc}
  
  add wave -group Ctrl -radix hexadecimal {sim:/tb_cpu/cpu0/ctrl_instance/dec}
  add wave -group Ctrl_temporary -radix hexadecimal {sim:/tb_cpu/cpu0/ctrl_instance/tmp_op1} {sim:/tb_cpu/cpu0/ctrl_instance/tmp_op2} {sim:/tb_cpu/cpu0/ctrl_instance/tmp_result} {sim:/tb_cpu/cpu0/ctrl_instance/tmp_carry}  
    run -all
}

proc rs {} {
	cc
	restart -f
	run -all 
}

proc qs {} {
	quit -sim
}