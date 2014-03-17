library ieee;
use ieee.std_logic_1164.all;

entity qmc is
	port  (
		i : in  std_logic_vector(2 downto 0);
		o : out std_logic_vector(2 downto 0)
	);
end qmc;

architecture ugly of qmc is
	signal a,b,c,d 	: std_logic;
	signal e,f 		: std_logic_vector(1 downto 0);
	
begin
  a	   <= c and i(1);
	b	   <= ((not i(2)) xor a);
	c	   <= i(1) nor not i(2);
	d	   <= e(0) nor (not e(1));
	e	   <= f xor (not a,b);
	f(0)	<= (b nand b) xor c;
	f(1)	<= (i(2) xor i(0)) or b;
	o(0)	<= not e(0);
	o(1)	<= a or (i(0) and f(1)); 
	o(2)	<= (a xor b) or (c and not d); 
end ugly;

architecture nice of qmc is
begin
  o(2) <= NOT i(2) OR (NOT i(1) AND NOT i(0));
  o(1) <= NOT i(2) AND i(0);
  o(0) <= i(2) AND NOT i(1);    
end nice;