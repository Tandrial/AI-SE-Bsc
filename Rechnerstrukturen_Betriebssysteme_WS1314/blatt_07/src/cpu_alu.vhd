library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

library work;
use work.cpu_pack.all;

entity cpu_alu is
	port  (
		signal mode			: in  alu_mode_t;
		signal op1,op2		: in  word_t;
		signal carry_in		: in  std_logic;
		signal result		: out word_t;
		signal carry_out	: out std_logic
	);
end cpu_alu;

architecture lowlevel of cpu_alu is

    subtype alu_word_t  is std_logic_vector(16 downto 0);

	-- Steuersignale fuer aus dem internen decoder fuer die 
	-- Multiplexer.
	type dec_t is record 
		inc_sel     : std_logic;                
		pre_inv     : std_logic_vector(1 to 2);
		path_inc    : std_logic;                
		path_ctrl   : std_logic;                
		path_sel    : std_logic;                
		post_inv    : std_logic;                
	end record;
	signal dec : dec_t; 
	
    -- Temporaere Signale innerhalb der der ALU
    signal tmp1a, tmp1b, tmp1c, tmp1d   : alu_word_t;
    signal tmp2a, tmp2b, tmp2c, tmpe    : alu_word_t;
    signal tmpm, tmp_r                  : alu_word_t;
    signal tmp_inc                      : std_logic;        


begin
	-- Der nachfolgende Prozess (Schaltnetz) decodiert die Steuersignale fuer die 
	-- Multiplexer innerhalb des eingeltichen ALU-Schaltnetzes aus der 
	-- ALU-Betriebsart (signal mode).
    -- Tabelle 2
	alu_dec : process (mode) is
	begin
		case mode is
			when ALU_MODE_ADD 	  => dec <= ('0',('0','0'),'0','1','0','0');
			when ALU_MODE_AND 	  => dec <= ('0',('0','0'),'0','0','0','0');
			when ALU_MODE_MUL     => dec <= ('0',('0','0'),'1','0','0','0');  
            when ALU_MODE_ADC 	  => dec <= ('1',('0','0'),'X','1','0','0');
			when ALU_MODE_SUB 	  => dec <= ('0',('0','1'),'1','1','0','0');
			when ALU_MODE_NEG 	  => dec <= ('0',('1','0'),'1','1','0','0');
			when ALU_MODE_OR  	  => dec <= ('0',('1','1'),'0','0','0','1');
			when ALU_MODE_NOR 	  => dec <= ('0',('1','1'),'0','0','0','0');
			when ALU_MODE_SHR 	  => dec <= ('0',('0','X'),'0','1','1','0');
			when ALU_MODE_SHL 	  => dec <= ('0',('0','X'),'0','0','1','0');
			when ALU_MODE_RCR 	  => dec <= ('1',('0','X'),'X','1','1','0');
			when ALU_MODE_RCL 	  => dec <= ('1',('0','X'),'X','0','1','0');			  
			when ALU_MODE_BYPASS  => dec <= ('X',('X','X'),'X','X','X','X');			  
			when others		      => dec <= ('X',('X','X'),'X','X','X','X');
		end case;  
	end process;	

    -- Dieser Prozess implementiert die eigentliche ALU (Schaltnetz).
    alu_comb : process(
            dec,						-- Signale des decoders
            carry_in, op1, op2,			-- Signale des Steuerwerks
            tmp1a,tmp1b,tmp1c,tmp1d, 	-- Temoraere 
            tmp2a,tmp2b,tmp2c,tmp_r,    -- Signale
            tmpm,tmpe,tmp_inc                         
            ) is
            
        function reverse(w : alu_word_t) return alu_word_t is   -- Diese Funktion spiegelt die
            variable i : integer;                               -- unteren 16 Bits in einem ALU-Wort
            variable r : alu_word_t;                            -- w.
        begin
            for i in 0 to 15 loop
                r(i) := w(15-i); 
            end loop;
            r(16) := w(16);
            return r;
        end;

        function mult(op1, op2 : alu_word_t) return alu_word_t is
            variable o1, o2 : std_logic_vector(15 downto 0);
            variable erg : std_logic_vector(31 downto 0);
        begin
            o1 := op1(15 downto 0);
            o2 := op2(15 downto 0); 
            erg := ZERO16 & ZERO16;                    
            for i in 0 to 15 loop
                if (o2(0) = '1') then
                    erg := std_logic_vector(signed(erg) + signed(o1));
                end if;
                o2 := "0" & o2(15 downto 1);
                o1 := o1(14 downto 0) & "0";      
            end loop;
        -- Schummeln????    
        -- erg := std_logic_vector(signed(op1(15 downto 0)) * signed(op2(15 downto 0))); 
            
            if (op1(15) = '1' xor op2(15) = '1') then
                if (erg(15) = erg(16)) then 
                    erg(16) := '0';
                else
                    erg(16) := '1';
                end if;   
            else
                if (erg(15) = '1') then
                    erg(16) := '1';
                else
                    erg(16) := '0';
                end if;   
            end if;
            return erg(16 downto 0);
        end;      

    begin
        if dec.inc_sel = '1' then  
            tmp_inc <= carry_in;
        else
            tmp_inc <= dec.path_inc;
        end if; 
            
        -- ab hier werden die Operanden auf 17 Bit erweitert und ggf. invertiert 

        -- op1 wird invertiert wenn pre_inv(1) gesetzt ist.
        if dec.pre_inv(1) = '0' then   
            tmp1a <= "0" & op1;
        else
            tmp1a <= "0" & (not op1);       -- Inverter
        end if;

        -- op2 wird invertiert wenn pre_inv(2) gesetzt ist.
        if dec.pre_inv(2) = '0' then   
            tmp2a <= "0" & op2;
        else
            tmp2a <= "0" & (not op2);       -- Inverter
        end if;     

        -- ab hier beginnt Pfad 1 (oben)

        --op1 wird gespiegelt wenn path_crtl gesetzt ist.
        if dec.path_ctrl = '0' then    
            tmp1b <= tmp1a;
        else
            tmp1b <= reverse(tmp1a);        -- "S"
        end if;

        tmp1c <= tmp1b(15 downto 0) & tmp_inc;                     -- "<<";"+1" 

        --op1 wird wieder gespiegelt (path_crlt == 1)
        if dec.path_ctrl = '0' then    
            tmp1d <= tmp1c;
        else
            tmp1d <= reverse(tmp1c);        -- "S"
        end if; 

        -- ab hier beginnt Pfad 0 (unten)
        tmp2b <= std_logic_vector(              
            unsigned(tmp1a) +                                      -- "+"
            unsigned(tmp2a) +       
            unsigned(ZERO16 & tmp_inc));                           -- "+1" 

        if dec.path_inc = '0' then
            tmpm <= tmp1a and tmp2a;                               -- "&"
        else
            tmpm <= mult(tmp1a, tmp2a);                            -- "*"
        end if;

        if dec.path_ctrl = '0' then    
            tmp2c <= tmpm;
        else  
            tmp2c <= tmp2b;
        end if;  

        -- ab hier werden die Pfade zum Ergebnis zusammengeführt 

        -- Auswahl zwischem oberen (path_sel == 1) und unterem (path_sel == 0) Pfad
        if dec.path_sel = '0' then     
            tmpe <= tmp2c;
        else
            tmpe <= tmp1d;
        end if;

        -- Ergebnis wird invertiert wenn post_inv gesetzt ist.
        if dec.post_inv = '0' then     
            tmp_r <= tmpe;
        else
            tmp_r <= not tmpe;              -- Inverter
        end if;     

        -- carry ist das 1 bit vom Ergebnis, result der Rest
        result      <= tmp_r(15 downto 0);
        carry_out   <= tmp_r(16);
    end process;
end lowlevel;