entity tb_w_enc is
end entity;

architecture arch of tb_w_enc is
  component w_enc is
  port  (Z : in  bit_vector(2 downto 0);
         lo, l, lu, m, ro, r, ru : out bit);
  end component;
  
 signal lo, l, lu, m, ro, r, ru : bit;
 signal Z: bit_vector(2 downto 0);     
  
 begin  
     w_enc_inst : w_enc port map(Z, lo, l, lu, m, ro, r, ru);
  process begin
    Z <= "000";
    wait for 10 ns;
    Z <= "001";
    wait for 10 ns;
    Z <= "010";
    wait for 10 ns;
    Z <= "011";
    wait for 10 ns;
    Z <= "100";
    wait for 10 ns;
    Z <= "101";
    wait for 10 ns;
    Z <= "110";
    wait for 10 ns;
    Z <= "111";
    wait;      
  end process;
end architecture;