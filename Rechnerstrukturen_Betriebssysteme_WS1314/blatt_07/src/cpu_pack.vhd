library ieee;
use ieee.std_logic_1164.all;

package cpu_pack is

    subtype word_t      is std_logic_vector(15 downto 0);   
    subtype alu_mode_t  is std_logic_vector( 3 downto 0);
    subtype ctrl_mode_t is std_logic_vector( 3 downto 0); 

    constant ZERO16         : word_t        := "0000000000000000";    
    constant DC16           : word_t        := "XXXXXXXXXXXXXXXX";

    constant ALU_MODE_ADD   	 : alu_mode_t    := "0000";  
    constant ALU_MODE_AND   	 : alu_mode_t    := "0001";        
    constant ALU_MODE_MUL   	 : alu_mode_t    := "0010";
    constant ALU_MODE_ADC   	 : alu_mode_t    := "0011";
    constant ALU_MODE_SUB   	 : alu_mode_t    := "0100";
    constant ALU_MODE_NEG   	 : alu_mode_t    := "0101";
    constant ALU_MODE_OR    	 : alu_mode_t    := "0110";
    constant ALU_MODE_NOR   	 : alu_mode_t    := "0111";
    constant ALU_MODE_SHR   	 : alu_mode_t    := "1000";
    constant ALU_MODE_SHL   	 : alu_mode_t    := "1001";
    constant ALU_MODE_RCR   	 : alu_mode_t    := "1010";
    constant ALU_MODE_RCL  	 	 : alu_mode_t	 := "1011";    
    constant ALU_MODE_BYPASS	 : alu_mode_t    := "1111";

	constant CTRL_MODE1			: ctrl_mode_t 	:= "0000";
	constant CTRL_MODE2			: ctrl_mode_t 	:= "0001";
	constant CTRL_MODE3			: ctrl_mode_t 	:= "0010";
	constant CTRL_MODE4			: ctrl_mode_t 	:= "0011";
	constant CTRL_MODE5			: ctrl_mode_t 	:= "0100";
	constant CTRL_MODE6			: ctrl_mode_t 	:= "0101";
	constant CTRL_MODE7			: ctrl_mode_t 	:= "0110";
	constant CTRL_MODE16		: ctrl_mode_t 	:= "0111";
	constant CTRL_MODE8			: ctrl_mode_t 	:= "1000";
	constant CTRL_MODE9			: ctrl_mode_t 	:= "1001";
	constant CTRL_MODE10		: ctrl_mode_t 	:= "1010";
	constant CTRL_MODE11		: ctrl_mode_t 	:= "1011";
	constant CTRL_MODE12		: ctrl_mode_t 	:= "1100";
	constant CTRL_MODE13		: ctrl_mode_t 	:= "1101";
	constant CTRL_MODE14		: ctrl_mode_t 	:= "1110";
	constant CTRL_MODE15		: ctrl_mode_t 	:= "1111";

	type status_t is record						-- Bits(Flags) des Statusregisters:
		carry 	: std_logic;					-- * Übertrag (Carry-Flag)
		zero 	: std_logic;					-- * Null (Zero-Flag)
	end record;	

	-- Symbolische Aufzaehlung der Befehlsphasen
	type phase_enum_t is (FETCH, DECODE, READ_MEM, OPERATE, WRITE_MEM);

	-- Registerzustand:

	type reg_t is record						-- Register des Prozessors:
		status 	: status_t; 					-- Prozessor-Statuswort
		acc		: word_t;						-- Akkumulator	 	(Accumulator)
		pc		: natural range 0 to 2**8-1;	-- Befehlszeiger 	(Program Counter)
		sp		: natural range 0 to 2**8-1;	-- Stack-Pointer
		inst 	: word_t;						-- Befehlsregister		
		mdr		: word_t;						-- Speicher-Datenregister
		mar		: natural range 0 to 2**9-1;	-- Speicher-Adressregister
		phase 	: phase_enum_t;					-- Ausführungsphase
	end record;
end package;