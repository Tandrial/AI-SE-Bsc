library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

library work;
use work.cpu_pack.all;

entity cpu_reg is
	port  (
		clk,reset 			: in  	std_logic;
		q					: out 	reg_t;
		d					: in 	reg_t
	);
end cpu_reg;

architecture arch of cpu_reg is

begin
	sync : process(clk,reset) is
	begin
		if reset='1' then
			q.status <= ('0','1');
			q.acc    <= ZERO16;
			q.pc     <= 0;
			q.sp	 <= 0;
			q.inst 	 <= ZERO16;					
			q.mdr 	 <= ZERO16;
			q.mar 	 <= 0;
			q.phase  <= FETCH;			
		elsif rising_edge(clk) then
			q <= d;
		end if;
	end process;
end arch;

