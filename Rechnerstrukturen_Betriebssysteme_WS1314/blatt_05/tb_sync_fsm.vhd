--Author: MKrane
library ieee;
use ieee.std_logic_1164.all;

entity tb_sync_fsm is
end entity;

architecture arch of tb_sync_fsm is

 signal ov1, ov2 : std_logic_vector(2 downto 0);   
 signal i : std_logic;
 signal clk : std_logic := '0';   
  
 begin  
     clk <= not clk after 100 ns;     
	
	v1 : entity work.sync_fsm(lowlevel_v1) 
      port map(clk, i, ov1);			
			
  v2 : entity work.sync_fsm(lowlevel_v2) 
      port map(clk, i, ov2);		  
			  
-- Alle Ausgaben werden einmal getestet:			  
sequence : process is
	begin		
--00
	  i <= '1';
   	wait until falling_edge(clk);   	
    assert ov1 = "101" report "Falscher Ausgangswert";
    assert ov2 = "101" report "Falscher Ausgangswert";

--01
    i <= '1';
    wait until falling_edge(clk);     
    assert ov1 = "101" report "Falscher Ausgangswert";         
    assert ov2 = "101" report "Falscher Ausgangswert";   

    i <= '0';
    wait until falling_edge(clk);     
    assert ov1 = "011" report "Falscher Ausgangswert";         
    assert ov2 = "011" report "Falscher Ausgangswert";   

--11
    i <= '1';
    wait until falling_edge(clk);     
    assert ov1 = "010" report "Falscher Ausgangswert";         
    assert ov2 = "010" report "Falscher Ausgangswert";   

--00
    i <= '0';
    wait until falling_edge(clk);     
    assert ov1 = "011" report "Falscher Ausgangswert";         
    assert ov2 = "011" report "Falscher Ausgangswert";   

--11
    i <= '0';
    wait until falling_edge(clk);     
    assert ov1 = "000" report "Falscher Ausgangswert";         
    assert ov2 = "000" report "Falscher Ausgangswert";   

--10
    i <= '0';
    wait until falling_edge(clk);     
    assert ov1 = "000" report "Falscher Ausgangswert";         
    assert ov2 = "000" report "Falscher Ausgangswert";   

    i <= '1';
    wait until falling_edge(clk);
    assert ov1 = "010" report "Falscher Ausgangswert";        
    assert ov2 = "010" report "Falscher Ausgangswert"; 
--00
		wait;
	end process; 
end architecture;