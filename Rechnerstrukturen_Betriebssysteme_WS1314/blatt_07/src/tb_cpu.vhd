library ieee;
use ieee.std_logic_1164.all;

entity tb_cpu is
	generic(INIT_FILE : string := "ram0.hex");
end tb_cpu;

architecture arch of tb_cpu is

	signal clk,clk_enable,reset				: std_logic := '0';

	signal mem_read,mem_write 				: std_logic := '0';
	signal mem_read_data,mem_write_data		: std_logic_vector(15 downto 0);
	signal mem_addr							: natural range 0 to 2**9-1 :=0;

	signal ram0_read,ram0_write 			: std_logic := '0';
	signal ram0_read_data,ram0_write_data	: std_logic_vector(15 downto 0);
	signal ram0_addr						: natural range 0 to 2**8-1 :=0;

	signal ram1_read,ram1_write 			: std_logic := '0';
	signal ram1_read_data,ram1_write_data	: std_logic_vector(15 downto 0);
	signal ram1_addr						: natural range 0 to 2**8-1 :=0;

begin

	clk 	<= not clk after 50 ns when clk_enable='1'; -- 10 MHz Takt
	
	cpu0 : entity work.cpu(structure)
	port map(
		clk            => clk,
		reset          => reset,
		mem_read       => mem_read,
		mem_write      => mem_write,
		mem_addr       => mem_addr,
		mem_read_data  => mem_read_data,
		mem_write_data => mem_write_data
	);
	
	memory : entity work.ram(sim)
	generic map(
		INIT_FILE     => INIT_FILE,
		USE_INIT_FILE => true,
		DATA_WIDTH    => 16,
		ADDR_WIDTH    => 8
	)
	port map(
		clk           => clk,
		address       => ram0_addr,
		write_data    => ram0_write_data,
		write         => ram0_write,
		read_data     => ram0_read_data
	);

	stack : entity work.ram(sim)
	generic map(
		USE_INIT_FILE => false,
		DATA_WIDTH    => 16,
		ADDR_WIDTH    => 8
	)
	port map(
		clk           => clk,
		address       => ram1_addr,
		write_data    => ram1_write_data,
		write         => ram1_write,
		read_data     => ram1_read_data
	);

	ram0_addr  			<= mem_addr mod 2**8;
	ram1_addr  			<= mem_addr mod 2**8;

	ram0_read  			<= mem_read  		when mem_addr <  2**8 else '0';
	ram0_write 			<= mem_write 		when mem_addr <  2**8 else '0';
	ram1_read  			<= mem_read  		when mem_addr >= 2**8 else '0';
	ram1_write 			<= mem_write 		when mem_addr >= 2**8 else '0';

	ram0_write_data 	<= mem_write_data;
	ram1_write_data 	<= mem_write_data;
	mem_read_data 		<= ram0_read_data when mem_addr < 2**8 else ram1_read_data;	
		
	
	
	clk_reset_ctrl : process is
	begin
		clk_enable <= '1';				-- aktiviert das Takt-Signal
		reset <= '1';	wait for 40 ns;	-- generiert das Reset-Signal
		reset <= '0';	
		
		wait until rising_edge(clk) and mem_addr=16#ff# and		-- Wartet darauf, dass die CPU  
				   mem_write='1' 	and mem_write_data=x"00FF"; -- 00FF an Adresse FF schreibt. 
		
		wait for 100 ns;	-- Wartet einen Befehlszyklus und
		clk_enable <='0';	-- hält den Takt an.
		
		wait;
	end process;
end arch;