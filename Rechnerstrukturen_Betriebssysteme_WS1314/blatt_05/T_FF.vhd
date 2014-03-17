library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity T_FF is
	generic (
		init 	: std_logic := '0';	--! Legt den initial gespeicherten Wert des FFs fest.
		delay	: time		:= 0 ns	--! Legt die Verzoegerung ab einer steigenden Taktflanke fest, ab dem der neue wert an den Ausgaengen sichtbar wird  
	);
	port  (
		clk 	: in  std_logic;	--! Taktsignal - an steigenden Flanken wird der neue Wert gespeichert
		t 		: in  std_logic;	--! Eingangssignal
		q,qn	: out std_logic		--! Ausgangssignale (qn ist der invertierte Wert)
	);
end T_FF;

architecture arch of T_FF is

	signal d : std_logic := init;
	
begin
	sync : process(clk) is
	begin
		if rising_edge(clk) then
			if 		t='0' then 	d <= d;
			elsif 	t='1' then 	d <= not d;
			else				d <= 'X';
			end if;
		end if;
	end process;
	
	q <= d after delay;
	qn <= not d after delay;
end arch;