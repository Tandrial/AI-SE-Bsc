library ieee;
use ieee.std_logic_1164.all;

entity tb_ctrl is
end;

architecture bench of tb_ctrl is

	signal clk		: std_logic := '0';
		
	signal cpu_r	: std_logic := '0';
	signal cpu_a	: std_logic_vector(1 downto 0) := "00";
	signal cpu_b	: std_logic := '0';
	signal cpu_d	: std_logic := '0';
	
	signal rom_s	: std_logic := '0';
	signal rom_a	: std_logic_vector(1 downto 0) := "00";
	signal rom_d	: std_logic := '1';
begin

	clk <= not clk after 100 ns; -- Entspr. 5 MHz. Ihre implementierung soll unabhaengig von der konkreten Taktrate sein!  
	
	dut : entity work.ctrl(arch) 
			port map(
				clk,cpu_r,cpu_a,cpu_b,cpu_d,rom_s,rom_a,rom_d 
			);
	
	sequence : process is
	begin
		
		-- Eingabe von Takt n (Stimuli) 
		wait until falling_edge(clk);
		cpu_r <= '1';
		cpu_a <= "10";
		
		-- Ueberpruefung der Ausgabe von Takt n (Response)
		wait until rising_edge(clk);
		assert cpu_b='1' report "Fehler, cpu_b != 1" severity warning; 
		assert rom_s='1' report "Fehler, rom_s != 1" severity warning; 
		
		-- Eingabe von Takt n+1 (Stimuli) 
		wait until falling_edge(clk);
		cpu_r <= '0';
		cpu_a <= "XX";
		
		wait until cpu_d = '1';
		rom_d <= '0';
		
				wait until falling_edge(clk);
		cpu_r <= '1';
		cpu_a <= "11";
		
				wait until rising_edge(clk);
		assert cpu_b='1' report "Fehler, cpu_b != 1" severity warning; 
		assert rom_s='1' report "Fehler, rom_s != 1" severity warning; 
		
		-- Eingabe von Takt n+1 (Stimuli) 
		wait until falling_edge(clk);
		cpu_r <= '0';
		cpu_a <= "XX";
		
		-- ... Ergaenzen Sie die Testbench nach eigenem Ermessen!
		wait;
	end process; 
end;