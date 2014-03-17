--Author: MKrane
library ieee;
use ieee.std_logic_1164.all;

entity tb_qmc is
end entity;

architecture arch of tb_qmc is

 signal i, oUgly, oNice: std_logic_vector(2 downto 0);     
  
 begin  
     tb_qmc_ugly : entity work.qmc(ugly) port map(i, oUgly);
     tb_qmc_nice : entity work.qmc(nice) port map(i, oNice);
  process begin
    I <= "000";
    wait for 10 ns;
    I <= "001";
    wait for 10 ns;
    I <= "010";
    wait for 10 ns;
    I <= "011";
    wait for 10 ns;
    I <= "100";
    wait for 10 ns;
    I <= "101";
    wait for 10 ns;
    I <= "110";
    wait for 10 ns;
    I <= "111";
    wait;      
  end process;
end architecture;