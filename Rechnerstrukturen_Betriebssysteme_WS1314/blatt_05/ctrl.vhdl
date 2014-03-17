library ieee;
use ieee.std_logic_1164.all;

entity ctrl is
	port(
		clk		: in std_logic;
		
		cpu_r	: in std_logic;
		cpu_a	: in std_logic_vector(1 downto 0);
		cpu_b	: out std_logic;
		cpu_d	: out std_logic;
		
		rom_s	: out std_logic;
		rom_a	: out std_logic_vector(1 downto 0);
		rom_d	: in std_logic
	);
end;

architecture arch of ctrl is
	signal zustand : std_logic_vector(1 downto 0) := "00";
	signal adr : std_logic_vector (1 downto 0);
	signal wert : std_logic := 'X';
	
	
begin
	speicherZugriff : process(clk, cpu_r) 
	begin	  
	  if rising_edge(cpu_r) then
      zustand <= "01";
      rom_s <= '1';
      cpu_b <= '1';
      adr <= cpu_a;
	  end if;
	  
	  if falling_edge(clk) then	    	    
	    if zustand = "00" AND cpu_r = '0' then 	    
	      cpu_d <= wert;
	      rom_a <= "XX";
 	      rom_s <= '0';
	      cpu_b <= '0';
	      
	    elsif zustand = "01" then
	      rom_a <= adr;
	      zustand <= "10";
	      
	    elsif zustand = "10" then	      
	      wert <= rom_d;
	      zustand <= "11";
	    
	    elsif zustand = "11" then 	
 	      cpu_b <= '0';
	      rom_s <= '0';    
	      cpu_d <= wert;
	      rom_a <= "XX";
	      zustand <= "00";
	    end if;   
      end if; 
    end process;	
end;