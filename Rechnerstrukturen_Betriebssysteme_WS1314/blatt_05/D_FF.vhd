
library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity D_FF is
	generic (
		init 	: std_logic := '0';	--! Legt den initial gespeicherten Wert des FFs fest.
		delay	: time		:= 0 ns	--! Legt die Verzoegerung ab einer steigenden Taktflanke fest, ab dem der neue wert an den Ausgaengen sichtbar wird  
	);
	port  (
		clk 	: in  std_logic;	--! Taktsignal - an steigenden Flanken wird der neue Wert gespeichert
		d 		: in  std_logic;	--! Eingangssignal
		q,qn	: out std_logic		--! Ausgangssignale (qn ist der invertierte Wert)
	);
end D_FF;

architecture arch of D_FF is
	signal dq : std_logic := init;
begin
	sync : process(clk) is
	begin
		if rising_edge(clk) then
			if d='0' or d='1' then 	dq <= d;
			else 					dq <= 'X';
			end if;
		end if;
	end process;
	
	q <= dq after delay;
	qn <= not dq after delay;
end arch;

