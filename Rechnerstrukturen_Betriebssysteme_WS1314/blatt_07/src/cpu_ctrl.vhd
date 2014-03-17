library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

library work;
use work.cpu_pack.all;

entity cpu_ctrl is
	port  (
		-- Signale zwischen Steuerwerk und Speicher 
		mem_read, mem_write	: out std_logic;					
		mem_addr			: out natural range 2**9-1 downto 0;
		mem_read_data		: in  std_logic_vector(15 downto 0);  
		mem_write_data		: out std_logic_vector(15 downto 0);
		
		-- Signale zwischen Steuerwerk und ALU
		alu_mode			: out alu_mode_t;
		alu_op1,alu_op2		: out word_t;
		alu_carry_in		: out std_logic;
		alu_result			: in  word_t;
		alu_carry_out		: in  std_logic;
		
		-- Signale zwischen Steuerwerk und Registerwerk
		reg_d				: out reg_t;	
		reg_q				: in  reg_t
	);
end cpu_ctrl;

architecture highlevel of cpu_ctrl is

	--PMEM benutzt den PC als Adresse zum laden des Operanden

	type update_condition_enum_t is (ALWAYS,CARRY,ZERO);				    	-- Sym. Datantypen zur Darstellung der 
	type operand_result_enum_t is (ACC,PC,INST,AMEM,RMEM,PMEM,SMEM,IREG,NONE);	-- Steuerleitungen des Steuerwerks. 

	constant IREG_PC 		: word_t 		:= "0000000000000000";	
	constant IREG_ACC 		: word_t 		:= "0000000000000001";	

	type dec_t is record 
		src1,src2,dest 		: operand_result_enum_t;
		update_condition	: update_condition_enum_t;                
		update_carry		: boolean;                
		operand				: word_t;
	end record;
	signal dec : dec_t; 

	-- Steuersignale des Steuerwerks, durch die Quelle und Ziel der Operanden
	-- bei einer Befehlsverarbeitung dargestellt werden. 
    --	signal dec.src1,dec.src2,dec.dest 	: operand_result_enum_t;
    --	signal dec.operand					: word_t;
    --	signal dec.update_condition					: dec.update_condition_enum_t;
    --	signal update_carry					: std_logic;
	 
	-- Temporaere Signale des Steuerwerks.
	signal tmp_op1,tmp_op2,tmp_result		: word_t;
	signal tmp_carry 						: std_logic;
	 
begin       
       
    --    
    ctrl_dec : process(reg_q) is
    begin
        
    	-- der nachfolgende Block decodiert die internen Steuerleitungen des 
    	-- Steuerwerks aus den oberen 4 Bit des Befehlsregisters.
		case reg_q.inst(15 downto 12) is	
			when CTRL_MODE1 => dec <= (ACC , INST, ACC , ALWAYS, false, DC16); 
			when CTRL_MODE2 => dec <= (ACC , INST, ACC , ALWAYS, true , DC16); 
			when CTRL_MODE3 => dec <= (INST, NONE, ACC , ALWAYS, false, DC16);
			when CTRL_MODE4 => dec <= (ACC , NONE, ACC , ALWAYS, false, DC16);
			when CTRL_MODE5 => dec <= (INST, PC  , PC  , ALWAYS, false, DC16);
			when CTRL_MODE6 => dec <= (INST, PC  , PC  , CARRY , false, DC16);
			when CTRL_MODE7 => dec <= (INST, PC  , PC  , ZERO  , false, DC16); 			
			when CTRL_MODE8 => dec <= (AMEM, ACC , ACC , ALWAYS, false, DC16);
			when CTRL_MODE9 => dec <= (ACC , AMEM, ACC , ALWAYS, true , DC16);
			when CTRL_MODE10=> dec <= (ACC , NONE, AMEM, ALWAYS, false, DC16);
			when CTRL_MODE11=> dec <= (RMEM, ACC , ACC , ALWAYS, false, DC16); 
			when CTRL_MODE12=> dec <= (ACC , RMEM, ACC , ALWAYS, true , DC16);
			when CTRL_MODE13=> dec <= (ACC , NONE, RMEM, ALWAYS, false, DC16);
			when CTRL_MODE14=> dec <= (SMEM, NONE, IREG, ALWAYS, false, DC16);
			when CTRL_MODE15=> dec <= (IREG, NONE, SMEM, ALWAYS, false, DC16);
            when CTRL_MODE16=> dec <= (ACC , PMEM, ACC , ALWAYS, true , DC16);                               
		                      
			when others 	=> dec <= (NONE, NONE, PC  , ALWAYS, false, DC16);
		end case;		
    	
        -- Dei nachfolgende Zuweisung decodiert den Befehlsoperanden aus den unteren
        -- 8 Bit des Befehlsregisters.
        -- Oder in MODE16 aus der Adresse des PC
    	
        if reg_q.inst(15 downto 12) = CTRL_MODE16 then
            dec.operand <= std_logic_vector(to_unsigned(reg_q.pc,16));            
        else
            dec.operand <= ZERO16(15 downto 8) & reg_q.inst(7 downto 0);          
        end if;

    end process; 

   	-- Die nachfolgende Zuweisung leitet die codierte ALU-Betriebsart aus den Bits
   	-- 8 bis 11 des Befehlsregisters an die ALU weiter. 
   	alu_mode <= reg_q.inst(11 downto 8);       
       
    -- Dieser Prozess implementiert das Schaltnetz des Steuerwerks.
    ctrl_comb : process (
    				reg_q,									-- Register ausgaben 
    				dec, 									-- Decoder
    				alu_carry_out, alu_result,				-- ALU
    				mem_read_data, 							-- Speicher
                	tmp_op1, tmp_op2, tmp_result, tmp_carry -- Temoraere Signale
                ) is
    begin
    	
    	-- Nachfolgend werden in alle Signale, die dieser Prozess erzeugt, auf Standardwerte gesetzt.
    	-- Im weiteren Verlauf dieses Prozesses werden diese Signale ggf. mit anderen Werten "überschrieben". 
        reg_d			<= reg_q;									-- Registerwerk         
        mem_read        <= '0';     	mem_write       <= '0';     -- Speicher
        mem_addr        <= reg_q.mar;	mem_write_data  <= DC16;    -- Speicher        
        alu_op1         <= DC16;    	alu_op2         <= DC16;    -- ALU   
        alu_carry_in    <= 'X';         	                        -- ALU        
        tmp_op1    		<= DC16;    	tmp_op2    		<= DC16;  	-- Temporaere in- 
        tmp_carry  		<= 'X';     	tmp_result 		<= DC16;    -- terne Signale

        -- Hier wird das ZERO-Flag berechnet.
        if reg_q.acc = ZERO16 then  
            reg_d.status.zero <= '1'; 
        else 
            reg_d.status.zero <= '0'; 
        end if;
        
        case reg_q.phase is 

            when FETCH =>       -- ## Befehlsholphase
                mem_read		<= '1'; -- Hinweis: MAR, und damit mem_addr, zeigt bereits auf die PC-Adresse                
              --mem_addr		<= reg_q.pc;
                
                -- Der nächste Block erhöht PC, wobei reg_q.PC erst in der nachfolgenden Befehlsphase
                -- den neuen Wert enthält. 
                if reg_q.pc < 2**8-1 then
                	reg_d.pc <= reg_q.pc + 1;
                else 
                    reg_d.pc <= 0;
                end if;                 

                reg_d.phase		<= DECODE;
                
            when DECODE =>      -- ## Befehlsdecodierphase
				-- Der nächste Block schreibt den geholten Befehl ins Befehlsregister.
				-- WICHTIG: die eigentliche Decodierung findet im Prozess ctrl_dec statt.
                reg_d.inst  <= mem_read_data;
                                
                reg_d.phase 	<= READ_MEM;
                         
            when READ_MEM =>    -- ## Lade-Phase von Speicheroperanden                  

                -- Falls noetig iniziiert der naechste Block iniziiert einen Lesezugriff 
                -- auf den speicher und schreibt die Adresse in das Speicheradressregister. 
				mem_read	<= '0';
                if dec.src1 = AMEM or dec.src2 = AMEM or dec.src2 = PMEM then    -- Abs. Adresse lesen
                    reg_d.mar   <= to_integer(unsigned(dec.operand));
                	mem_addr	<= to_integer(unsigned(dec.operand));
                	mem_read	<= '1';

                elsif dec.dest = AMEM then      			                     -- Abs. Adresse schreiben  
                    reg_d.mar   <= to_integer(unsigned(dec.operand));

				elsif dec.src1 = RMEM or dec.src2 = RMEM then	                 -- Rel. Adr. lesen
					reg_d.mar	<= (to_integer(unsigned(dec.operand))+reg_q.PC) mod 2**8; 
                	mem_addr	<= (to_integer(unsigned(dec.operand))+reg_q.PC) mod 2**8;
    				mem_read	<= '1';

				elsif dec.dest= RMEM then						                 -- Rel. Adr. schreiben
					reg_d.mar	<= (to_integer(unsigned(dec.operand))+reg_q.PC) mod 2**8;

                elsif dec.src1 = SMEM or dec.src2 = SMEM then                    -- Stack lesen (POP) 
                    reg_d.mar   <= reg_q.sp + 2**8-1;
	            	mem_addr	<= reg_q.sp + 2**8-1;
    				mem_read	<= '1';

                elsif dec.dest = SMEM then                                       -- Stack schreiben (PUSH)
                    reg_d.mar   <= reg_q.sp + 2**8;
                end if;

                reg_d.phase		<= OPERATE;
                
            when OPERATE =>     -- ## Verknüpfung
                
                -- Der nächste Block ermittelt den Wert von 
                -- Op. 1 und setzt tmp_op1 dementsprechend. 
                
                -- Fall: Akkumulator
                if dec.src1 = ACC or (dec.src1 = IREG and dec.operand = IREG_ACC) then
                    tmp_op1 <= reg_q.acc;
                
                -- Fall: Befehlszeiger
                elsif dec.src1 = PC  then				
                    tmp_op1 <= std_logic_vector(to_unsigned(reg_q.pc,16));             
                
                -- Fall: Befehlszeiger+1
                elsif dec.src1 = IREG and dec.operand = IREG_PC then
                    tmp_op1 <= std_logic_vector(to_unsigned(reg_q.pc+1,16));

                -- Fall: Befehlsoperand
                elsif dec.src1 = INST then
                    tmp_op1 <= dec.operand;

                -- Fall: Speicher
                elsif dec.src1 = AMEM or dec.src1 = RMEM or dec.src1 = SMEM then
                    tmp_op1 <= mem_read_data;

                else    
                    tmp_op1 <= ZERO16;
                end if;
                
                -- Der nächste Block ermittelt den Wert von 
                -- Op. 2 und setzt tmp_op2 dementsprechend.

                -- Fall: Akkumulator
                if dec.src2 = ACC or (dec.src2 = IREG and dec.operand = IREG_ACC) then
                    tmp_op2 <= reg_q.acc;  

                -- Fall: Befehlszeiger
                elsif dec.src2 = PC then 
                    tmp_op2 <= std_logic_vector(to_unsigned(reg_q.pc,16));             

                -- Fall: Befehlszeiger+1
                elsif dec.src2 = IREG and dec.operand = IREG_PC then
                    tmp_op2 <= std_logic_vector(to_unsigned(reg_q.pc+1,16));

                -- Fall: Befehlsoperand
                elsif dec.src2 =  INST then
                    tmp_op2 <= dec.operand;
                
                -- Fall: Speicher
                elsif dec.src2 = AMEM or dec.src2 = RMEM or dec.src2 = SMEM or dec.src2 = PMEM then
                    tmp_op2 <= mem_read_data;
                else
                    tmp_op2 <= ZERO16;                                             
                end if;
                
                -- Der nächste Block verknüpft tmp_op1 und tmp_op2 per ALU zum Ergebnis 
                -- in tmp_result/tmp_carry oder leitet op1/das Carry-Flag darauf weiter 
                -- (BYPASS). 
                if reg_q.inst(11 downto 8) /= ALU_MODE_BYPASS then  
                    alu_op1         <= tmp_op1;            
                    alu_op2         <= tmp_op2; 
                    alu_carry_in    <= reg_q.status.carry;
                    tmp_result 		<= alu_result;
                    tmp_carry  		<= alu_carry_out;  
                else   
                    tmp_result 		<= tmp_op1; 
                    tmp_carry  		<= reg_q.status.carry;
                end if;

                -- Der nächste Block schreibt die Ergebnisse in ein Register zurück, ermittelt
                -- die nachfolgende Befehlsphase und aktualisiert das Carry-Flag.   
                if  dec.update_condition   = ALWAYS    or  
                    (dec.update_condition  = CARRY     and reg_q.status.carry = '1') or
                    (dec.update_condition  = ZERO      and reg_q.status.zero = '1') 
                then
                    if dec.dest = ACC or (dec.dest = IREG and dec.operand = IREG_ACC) then
                        reg_d.acc <= tmp_result;   

                    elsif dec.dest = PC or (dec.dest = IREG and dec.operand = IREG_PC) then
                        reg_d.pc  <= to_integer(unsigned(tmp_result)) mod 2**8;    

                    elsif dec.dest = AMEM or dec.dest = RMEM or dec.dest = SMEM then
                        reg_d.mdr <= tmp_result;
                    end if;                 

                    if dec.update_carry then 
                        reg_d.status.carry <= tmp_carry; 
                    end if;
                end if;
                
                -- Der nächste Block verringert oder erhoeht SP bei Stack-zugriffen.
                if  dec.src1 = SMEM or dec.src2 = SMEM then                     --POP
                    if reg_q.sp > 0 then 
                        reg_d.sp <= reg_q.sp - 1; 
                    end if;
                elsif dec.dest = SMEM then                                      --PUSH
                    if reg_q.sp < 2**8-1 then   
                        reg_d.sp <= reg_q.sp + 1; 
                    end if;
                end if;   

                if dec.src2 = PMEM then
                    reg_d.pc <= reg_q.pc + 1;
                end if; 
                
                reg_d.phase <= WRITE_MEM;
                
            when WRITE_MEM =>   -- ## Schreibphase
               	
                if dec.dest = AMEM or dec.dest = RMEM or dec.dest = SMEM then
               		mem_addr        <= reg_q.mar;
               		mem_write_data  <= reg_q.mdr;
                	mem_write       <= '1';
                end if;      

                reg_d.mar		<= reg_q.pc;
                
                reg_d.phase 	<= FETCH;
            when others =>  
        end case;
    end process;
end highlevel;