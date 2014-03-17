library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;
use ieee.std_logic_textio.all;

library std;
use std.textio.all;

entity ram is
    generic 
    (
        INIT_FILE     : string            := "RAM.hex";
        USE_INIT_FILE : boolean 		  := false;
        DATA_WIDTH    : natural           := 16;
        ADDR_WIDTH    : natural           := 10
    );

    port 
    (
        clk        : in  std_logic;
        address    : in  natural range 0 to 2**ADDR_WIDTH - 1;
        write_data : in  std_logic_vector((DATA_WIDTH-1) downto 0);
        write      : in  std_logic := '1';
        read_data  : out std_logic_vector((DATA_WIDTH -1) downto 0)
    );
end ram;


architecture rtl of ram is

    -- Build a 2-D array type for the RAM
    subtype word_t is std_logic_vector((DATA_WIDTH-1) downto 0);
    type memory_t is array(0 to 2**ADDR_WIDTH-1) of word_t;

    -- Declare the RAM signal.  
    signal ram : memory_t;

    -- Register to hold the addressess 
    signal address_reg : natural range 0 to 2**ADDR_WIDTH-1;

begin

    process(clk)
    begin
    if(rising_edge(clk)) then
        if(write = '1') then
            ram(address) <= write_data;
        end if;

        -- Register the address for reading
        address_reg <= address;
    end if;
    end process;

    read_data <= ram(address_reg);
end;


architecture sim of ram is

    -- Build a 2-D array type for the RAM
    subtype word_t is std_logic_vector((DATA_WIDTH-1) downto 0);
    type memory_t is array(0 to 2**ADDR_WIDTH-1) of word_t;

    -- Declare the RAM signal.  
    signal ram : memory_t;

    -- Register to hold the address 
    signal address_reg : natural range 0 to 2**ADDR_WIDTH-1;

begin

  process(clk)
  
    -- RTL_SYNTHESIS OFF
    -- pragma translate_off        
    variable is_initialized : boolean := false;

    function hex_to_int(c : character) return integer is
    begin
    	case c is
				when '0' => return 0;  when '1' => return 1;  when '2' => return 2;  when '3' => return 3;  when '4' => return 4;
				when '5' => return 5;  when '6' => return 6;  when '7' => return 7;  when '8' => return 8;  when '9' => return 9;
				when 'a' => return 10; when 'b' => return 11; when 'c' => return 12; when 'd' => return 13; when 'e' => return 14; when 'f' => return 15;
				when 'A' => return 10; when 'B' => return 11; when 'C' => return 12; when 'D' => return 13; when 'E' => return 14; when 'F' => return 15;
 				when others => return -1;
 			end case;
 			return -1;
 		end;
      

    --! @param cnt The number of nibbles/hex characters to read.
    --! @param dst The destination vector, must have cnt*4 elements.
    --! @param src The line to read from.
    --! @param success true of reading was successful, false if an error occured.
    procedure read_hex(src : inout line; dst : out std_logic_vector; cnt : in positive; success : out boolean) is
    	variable neol : boolean;
    	variable char : character;
    	variable x    : integer;
    	variable nib  : std_logic_vector(3 downto 0);

    begin
    	success := false;

    	for i in 0 to cnt-1 loop
    		read(src,char,neol); --TODO End of line checks
    		x := hex_to_int(char);
    		if x < 0 then
    			report "Invalid character!" severity error;
    			exit;
    		end if;
    		nib := std_logic_vector(to_unsigned(x,4));

    		if dst'left > dst'right then
    			dst(dst'left-i*4 downto dst'left-i*4-3) := nib;
    		else
    			dst(dst'left+i*4 to dst'left+i*4+3) := nib;
    		end if;
    	end loop;

    	success := true;
    end;

    procedure read_hex(src : inout line; dst : out natural; cnt : in positive; success : out boolean) is
        variable tmp : std_logic_vector(cnt*4-1 downto 0);
        
    begin
        read_hex(src,tmp,cnt,success);
        dst := to_integer(unsigned(tmp));
    end;
    
    -- TODO Correct end of line checks
    procedure read_init_file is

    	file ihex_file          : text;
    	variable ihex_line      : line;
    	variable c              : character;
    	variable line_count     : natural := 0;
    	variable line_byte_count : natural;
    	variable line_address   : natural;
    	variable line_type      : natural;
    	variable line_data      : std_logic_vector(DATA_WIDTH-1 downto 0);
    	variable line_chk       : natural;
			variable bin_address		: std_logic_vector(((ADDR_WIDTH+7)/8)*8-1 downto 0);
			
    	variable chk            : natural;

    	variable l,r,tmp        : integer;
    	variable NEOL,ok        : boolean;

    begin
    	file_open(ihex_file,INIT_FILE,READ_MODE);

    	while not endfile(ihex_file) loop
				
    		readline(ihex_file,ihex_line);
    		line_count := line_count + 1;
    		
    		NEOL := false;

        -- ihex_line'length < (2 + 4 + 2 + (DATA_WIDTH/4) + 2) then -- Note: line'length decreases during read(...) to the remaining characters..
    		read(ihex_line,c,NEOL);

    		ok := (c = ':') or (c = '@');    		
    		assert ok report "No initial ':' or '@' character found in line " & natural'image(line_count) & " of file " & INIT_FILE & ", line skipped!" severity warning;
    		next when not ok;

				if c = ':' then
	    		ok := ihex_line'length >= 10;
	    		assert ok report "Line " & natural'image(line_count) & " of file " & INIT_FILE & " is to short, line skipped!" severity warning;
	    		next when not ok;
	
	    		chk := 0;
	
	        -- Liest die laenge der Daten in line_byte_count
	    		read_hex(ihex_line,line_byte_count,2,ok);
	    		assert ok report "Failed to read byte count field in line " & natural'image(line_count) & " of file " & ", line skipped!" severity warning;
	    		chk := chk + line_byte_count;
	
	        -- Liest die Adresse des Datensatzes in line_address
	    		read_hex(ihex_line,line_address,4,ok);
	    		assert ok report "Failed to read address field in line " & natural'image(line_count) & " of file " & ", line skipped!" severity warning;
	    		next when not ok;
	    		chk := chk + (line_address / 256) + (line_address mod 256);
	
	        -- Liest den Typ des Datensatzes in line_type
	    		read_hex(ihex_line,line_type,2,ok);
	    		assert ok report "Failed to read type field in line " & natural'image(line_count) & " of file " & ", line skipped!" severity warning;
	    		next when not ok;
	    		chk := chk + line_type;
	
	        -- Liest die datenbytes in line_data
	    		if line_byte_count > 0 and line_type = 0 then
	    			for i in line_byte_count-1 downto 0 loop
	    				r := i * 8; l := r + 7;
	    				if l > line_data'left then
	    					l := line_data'left;
	    				end if;
	    				read_hex(ihex_line,tmp,2,ok);
	    				exit when not ok;
	    				line_data(l downto r) := std_logic_vector(to_unsigned(tmp,l-r+1));
	    				chk := chk + tmp;
	    			end loop;
	    			assert ok report "Failed to read data field in line " & natural'image(line_count) & " of file " & ", line skipped!" severity warning;
	    			next when not ok;
	    		end if;
	
					-- Liest die Pruefsumme in line_chk
	    		read_hex(ihex_line,line_chk,2,ok);
	    		assert ok report "Failed to read checksum field in line " & natural'image(line_count) & " of file " & ", line skipped!" severity warning;
	    		next when not ok;
	
	    		ok := (chk + line_chk) mod 256 = 0;
	    		assert ok report "Wrong checksum in line " & natural'image(line_count) & " of file " & INIT_FILE & ", line skipped!" severity warning;
	    		next when not ok;
	
	    		if line_byte_count > 0 and line_type = 0 then
	    			ram(line_address) <= line_data;
	    		end if;
				else
					hread(ihex_line,bin_address,ok);
					assert ok report "Invalid address information in line " & natural'image(line_count) & " of file " & INIT_FILE & ", line skipped!" severity warning;
					next when not ok;

					line_address := to_integer(unsigned(bin_address));
					ok := line_address < 2**ADDR_WIDTH;
					assert ok report "Address exceeds " & natural'image(line_address) & " in line " & natural'image(line_count) & " of file " & INIT_FILE & ", line skipped!" severity warning;
					next when not ok;

					read(ihex_line,c,ok);					
					ok := (c = ':');
					assert ok report "Missing ':' after address in line " & natural'image(line_count) & " of file " & INIT_FILE & ", line skipped!" severity warning;
					next when not ok;
										
					
					hread(ihex_line,line_data,ok);
					assert ok report "Couldn't read data in line " & natural'image(line_count) & " of file " & INIT_FILE & ", line skipped!" severity warning;
					next when not ok;
					
					ram(line_address) <= line_data;
					
				end if;
    	end loop;
    	file_close(ihex_file);
    end;

    -- RTL_SYNTHESIS ON
    -- pragma translate_on        

	begin
    
    -- pragma translate_off
    -- -- RTL_SYNTHESIS OFF
    
    if (not is_initialized) then
    		for i in 0 to 2**ADDR_WIDTH-1 loop
    			ram(i) <= (others => '0');
    		end loop;
        report "Reading ram initialization file " & INIT_FILE & "...";
        
        if (USE_INIT_FILE) then
        	read_init_file;
        end if;
        is_initialized := true;
        report "Ram initialized.";
    end if; 
    -- pragma translate_on
    -- RTL_SYNTHESIS ON
    
    if(rising_edge(clk)) then
      if(write = '1') then
        ram(address) <= write_data;
      end if;

      -- Register the addressess for reading
      address_reg <= address;
    end if;
    end process;

    read_data <= ram(address_reg);
end;