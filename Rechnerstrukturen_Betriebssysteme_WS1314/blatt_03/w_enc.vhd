entity w_enc is
  port (Z : in  bit_vector(2 downto 0);
        lo,l,lu,m,ro,r,ru : out bit);
  end w_enc;

architecture arch of w_enc is
  signal a, b : bit;
  
begin
  a <= z(2) AND z(1);
  b <= z(2) OR z(1); 
  
  lo <= z(2);
  l <= a; 
  lu <= b; 
  m <= z(0);
  ro <= b;
  r <= a;
  ru <= z(2);  
end arch;