library ieee;
use ieee.std_logic_1164.all;

library work;
use work.cpu_pack.all;


entity cpu is
	port  (
		clk,reset 			: in  std_logic;
		mem_read, mem_write	: out std_logic;					
		mem_addr			: out natural range 2**9-1 downto 0;
		mem_read_data		: in  std_logic_vector(15 downto 0);  
		mem_write_data		: out std_logic_vector(15 downto 0) 		
	);
end cpu;

architecture structure of cpu is

		-- Signale zwischen Steuerwerk und ALU
	signal alu_mode			: alu_mode_t;
	signal alu_op1,alu_op2	: word_t;
	signal alu_carry_in		: std_logic;
	signal alu_result		: word_t;
	signal alu_carry_out	: std_logic;
		
		-- Signale zwischen Steuerwerk und Registerwerk
	signal reg_d			: reg_t;	
	signal reg_q			: reg_t;
	
begin

	-- Instanziiert das Registerwerk
	reg_instance : entity work.cpu_reg(arch)
	port map(
		clk   => clk,
		reset => reset,
		d     => reg_d,
		q     => reg_q
	);
	
	-- Instanziiert die ALU
	alu_instance : entity work.cpu_alu(lowlevel)
	port map(
		mode      => alu_mode,
		op1       => alu_op1,
		op2       => alu_op2,
		carry_in  => alu_carry_in,
		result    => alu_result,
		carry_out => alu_carry_out
	);
	
	-- Instanziiert das Steuerwerk 
	ctrl_instance : entity work.cpu_ctrl(highlevel)
	port map(
		mem_read       => mem_read,
		mem_write      => mem_write,
		mem_addr       => mem_addr,
		mem_read_data  => mem_read_data,
		mem_write_data => mem_write_data,
		alu_mode       => alu_mode,
		alu_op1        => alu_op1,
		alu_op2        => alu_op2,
		alu_carry_in   => alu_carry_in,
		alu_result     => alu_result,
		alu_carry_out  => alu_carry_out,
		reg_d          => reg_d,
		reg_q          => reg_q
	);
end structure;