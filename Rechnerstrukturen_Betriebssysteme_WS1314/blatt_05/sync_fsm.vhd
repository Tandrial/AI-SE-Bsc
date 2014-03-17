library ieee;
use ieee.std_logic_1164.all;

entity sync_fsm is
	port  (
		clk : in std_logic; 					-- Taktsignal
		i	: in std_logic; 					-- Eingabewert
		q	: out std_logic_vector(2 downto 0)	-- Ausgabevektor (LSB ist index 0)
	);
end sync_fsm;

architecture lowlevel_v1 of sync_fsm is

	signal d,t,qn_d,q_d,qn_t,q_t : std_logic;	
	
	signal z: std_logic_vector(1 downto 0) := "00";
	
	begin

	t_ff0 : entity work.t_ff(arch)
	generic map(
		init  => '0',
		delay => 10 ns
	)
	port map(
		clk   => clk,
		t     => t,
		q     => q_t,
		qn    => qn_t
	);
	
	d_ff0 : entity work.d_ff(arch)
	generic map(
		init  => '0',
		delay => 10 ns
	)
	port map(
		clk   => clk,
		d     => d,
		q     => q_d,
		qn    => qn_d
	);
	
   q(2) <= NOT z(1) AND i;
   q(1) <= (i AND z(1)) OR (NOT i AND NOT z(1));
   q(0) <= NOT z(1);
	
  d <= NOT z(1);	   	 
  t <= z(1) XOR NOT i;   

	sync : process(clk)
	begin	 
	 if falling_edge(clk) then
 	   z(1) <= q_t;	   	 
	   z(0) <= q_d;
	 end if; 
	end process;
end lowlevel_v1;

architecture lowlevel_v2 of sync_fsm is

	signal r,s,j,k,qn_jk,q_jk,qn_rs,q_rs : std_logic;
	signal z: std_logic_vector(1 downto 0) := "00";
	
begin

	jk_ff0 : entity work.jk_ff(arch)
	generic map(
		init  => '0',
		delay => 10 ns
	)
	port map(
		clk   => clk,
		j     => j,
		k     => k,
		q     => q_jk,
		qn    => qn_jk
	);
	
	rs_ff0 : entity work.rs_ff(arch)
	generic map(
		init  => '0',
		delay => 10 ns
	)
	port map(
		clk   => clk,
		r     => r,
		s     => s,
		q     => q_rs,
		qn    => qn_rs
	);
	
   q(2) <= NOT z(1) AND i;
   q(1) <= (i AND z(1)) OR (NOT i AND NOT z(1));
   q(0) <= NOT z(1);

  s <= NOT z(1);     	 
  r <= z(1);  
  
  j <= NOT i;
  k <= i;

  sync : process(clk)
    begin	 
	  if falling_edge(clk) then
 	    z(1) <= q_jk;	   	 
	    z(0) <= q_rs;
      end if; 
  end process;
end lowlevel_v2;