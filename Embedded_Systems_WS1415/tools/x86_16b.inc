;----------------------------------------------------------------------------
;
; Definition file for x86 instructions in 16 bits mode.
;
; Author: Hugues Smeets.
; Initial Date:   May 28 2010.
; Date: August 19 2010.
; Change log:
;----------------------------------------------------------------------------

;----------------------------------------------------------------------------
; General-purpose macros.
;----------------------------------------------------------------------------

.macro DUP_ON()      .allowDuplicateSymbols
.macro DUP_OFF()     .noDuplicateSymbols

; For now, to define a variable, use 'var_name: <value>'... And do not use
; forward references! The enstructions cannot handle forward references
; properly: some option must be implemented to allow assuming an unknown
; expression is known, even at pass 1, when contains forward references.


;.macro VAR_INIT(x) DUP_ON() _VA = x DUP_OFF()
;.macro DB(x) x = _VA DUP_ON() _VA = _VA + 1 DUP_OFF()
;.macro DW(x) x = _VA DUP_ON() _VA = _VA + 2 DUP_OFF()
;.macro DD(x) x = _VA DUP_ON() _VA = _VA + 4 DUP_OFF()
;.macro DQ(x) x = _VA DUP_ON() _VA = _VA + 8 DUP_OFF()


.macro char(x) x
.macro lchar(x) x

.macro GET_SIZE(x) ( (x) .and ( SIZE_16 .or SIZE_8 .or SIZE_32 ) )
.macro GET_MOD(x)  (( (x)>>25 ) .and 3)

.macro WORD(x) (x) ((x)>>8)
.macro BYTE(x) x

.macro IS_REG(x)   (( (x) .and REG_TAG) == REG_TAG)
.macro IS_SREG(x)  (( (x) .and SREG_TAG) == SREG_TAG)
.macro IS_MEM(x)   (( (x) .and MEM_TAG) == MEM_TAG)
.macro IS_DISP8(x) ( GET_MOD(x) == GET_MOD(MOD_DISP8) )
.macro IS_DISP16(x)( GET_MOD(x) == GET_MOD(MOD_DISP16) )
.macro IS_DIR16(x) (( (x)>>30 ) .and 1)
.macro IS_IM8(x)   (( (x)>>19 ) .and 1)
.macro IS_IM16(x)  (( (x)>>20 ) .and 1)
.macro IS_WORD(x)  ( ((x) .and SIZE_16) == SIZE_16)
.macro IS_NOT(x)   ( (x) == 0)
.macro IS_UNKNOWN(x) (IS_NOT(IS_REG(x)) .and IS_NOT(IS_SREG(x)) .and \
                      IS_NOT(IS_MEM(x)) .and IS_NOT(IS_IM8(x)) .and \
                      IS_NOT(IS_IM16(x)) )
.macro GET_REG(x)  (( (x)>>16 ) .and 7)
.macro GET_SREG(x) (( (x)>>16 ) .and 7)
.macro GET_RM(x)   (( (x)>>16 ) .and 7)
.macro GET_IM8(x)  ( (x) .and   0xFF)
.macro GET_IM16(x) ( (x) .and 0xFFFF)

.macro MAKE_REG16(x) ( ((x) << 16 ) + REG_TAG + MOD_REG + SIZE_16 )
.macro MAKE_REG8(x)  ( ((x) << 16 ) + REG_TAG + MOD_REG + SIZE_8 )

.macro FILL_UNTIL(n,f) f \
  .if . == n done \
  FILL_UNTIL(n,f)  \
  .metalabel done

.macro MOD_RM(reg,rm) \
  ((GET_MOD(rm)<<6)+(GET_REG(reg)<<3)+GET_RM(rm))

.macro ORIENTED_MOD_RM(dest,src) \
 IS_MEM(dest)*IS_REG(src)*MOD_RM(src,dest)+ \
 IS_MEM(src)*IS_REG(dest)*MOD_RM(dest,src)+ \
 IS_REG(src)*IS_REG(dest)*MOD_RM(src,dest)

.macro MOV_OP_MODRM(op,dest,src) \
 op                                     +   \  opcode        (bits 7..2)              OPCODE byte
  (IS_WORD(dest) .or IS_WORD(src))      +   \  size bit      (bit 0)
  (IS_MEM(src) .or IS_SREG(dest))*2         \  direction bit (bit 1)
                                            \
 ORIENTED_MOD_RM(dest,src)              +   \                                         MOD/RM byte
 IS_SREG(src)*MOD_RM(src,dest)          +   \
 IS_SREG(dest)*MOD_RM(dest,src)             \
 ENCODE_DISP(dest,src)                      ;                                         DISPLACEMENT (if any).

.macro SIZE_DIR_OP_MODRM(op,dest,src) \
 op                                     +   \  opcode        (bits 7..2)
 (IS_WORD(dest) .or IS_WORD(src))       +   \  size bit      (bit 0)
 IS_MEM(src)*2                              \ direction bit (bit 1)
 ORIENTED_MOD_RM(dest,src)                  \
 ENCODE_DISP(dest,src)                      ;                                         DISPLACEMENT (if any).

.macro SIZE_OP_MODRM(op,dest,src) \
 op                                     +   \  opcode        (bits 7..2)
 (IS_WORD(dest) .or IS_WORD(src))           \  size bit      (bit 0)
 ORIENTED_MOD_RM(dest,src)                  \
 ENCODE_DISP(dest,src)                      ;                                         DISPLACEMENT (if any).


.macro OP_MODRM(op,dest,src) op MOD_RM(dest,src)

;----------------------------------------------------------------------------
; Type tags to make instruction encoding as polymorphic as possible.
;----------------------------------------------------------------------------
;              3 2 2 2 2 2 1 1 1 1 1 9 7 5 3 1
;              1 9 7 5 3 1 9 7 5 3 1  8 6 4 2 0
DIR16_TAG  = 0b01000000000000000000000000000000
MEM_TAG    = 0b00100000000000000000000000000000
SREG_TAG   = 0b00010000000000000000000000000000
REG_TAG    = 0b00001000000000000000000000000000
MOD_DISP16 = 0b00000100000000000000000000000000
MOD_DISP8  = 0b00000010000000000000000000000000
MOD_REG    = 0b00000110000000000000000000000000
SIZE_16    = 0b00000001000000000000000000000000
SIZE_8     = 0b00000000100000000000000000000000
SIZE_32    = 0b00000000010000000000000000000000     ; hack to tag 32-bit registers.

; used for register identification:
;            0b-------------XXX----------------

.macro IM8(x) \
  ( (x) .or  0b00000000000010000000000000000000 .or SIZE_8 )

.macro IM16(x) \
  ( (x) .or  0b00000000000100000000000000000000 .or SIZE_16 )

;----------------------------------------------------------------------------
; constants for the REG and RM fields
;----------------------------------------------------------------------------
AX = 0x00000 + SIZE_16 + MOD_REG + REG_TAG
CX = 0x10000 + SIZE_16 + MOD_REG + REG_TAG
DX = 0x20000 + SIZE_16 + MOD_REG + REG_TAG
BX = 0x30000 + SIZE_16 + MOD_REG + REG_TAG
SP = 0x40000 + SIZE_16 + MOD_REG + REG_TAG
BP = 0x50000 + SIZE_16 + MOD_REG + REG_TAG
SI = 0x60000 + SIZE_16 + MOD_REG + REG_TAG
DI = 0x70000 + SIZE_16 + MOD_REG + REG_TAG

AL = 0x00000 + SIZE_8 + MOD_REG + REG_TAG
CL = 0x10000 + SIZE_8 + MOD_REG + REG_TAG
DL = 0x20000 + SIZE_8 + MOD_REG + REG_TAG
BL = 0x30000 + SIZE_8 + MOD_REG + REG_TAG
AH = 0x40000 + SIZE_8 + MOD_REG + REG_TAG
CH = 0x50000 + SIZE_8 + MOD_REG + REG_TAG
DH = 0x60000 + SIZE_8 + MOD_REG + REG_TAG
BH = 0x70000 + SIZE_8 + MOD_REG + REG_TAG

EAX = 0x00000 + SIZE_32 + MOD_REG + REG_TAG
ECX = 0x10000 + SIZE_32 + MOD_REG + REG_TAG
EDX = 0x20000 + SIZE_32 + MOD_REG + REG_TAG
EBX = 0x30000 + SIZE_32 + MOD_REG + REG_TAG
ESP = 0x40000 + SIZE_32 + MOD_REG + REG_TAG
EBP = 0x50000 + SIZE_32 + MOD_REG + REG_TAG
ESI = 0x60000 + SIZE_32 + MOD_REG + REG_TAG
EDI = 0x70000 + SIZE_32 + MOD_REG + REG_TAG

ES = 0x00000 + SREG_TAG + SIZE_16
CS = 0x10000 + SREG_TAG + SIZE_16
SS = 0x20000 + SREG_TAG + SIZE_16
DS = 0x30000 + SREG_TAG + SIZE_16

;----------------------------------------------------------------------------
; more constants for RM field
;----------------------------------------------------------------------------
AT_BX_SI  = 0 + MEM_TAG
AT_BX_DI  = 0x10000 + MEM_TAG
AT_BP_SI  = 0x20000 + MEM_TAG
AT_BP_DI  = 0x30000 + MEM_TAG
AT_SI     = 0x40000 + MEM_TAG
AT_DI     = 0x50000 + MEM_TAG
.macro AT_DISP16(disp) (0x60000+ MEM_TAG + DIR16_TAG + ((disp) & 0xFFFF))
AT_BX     = 0x70000 + MEM_TAG

.macro AT_IND_DISP8(regs,disp) \
  (regs + MOD_DISP8 + disp)

.macro AT_BX_SI_DISP8(disp) AT_IND_DISP8(AT_BX_SI,disp)
.macro AT_BX_DI_DISP8(disp) AT_IND_DISP8(AT_BX_DI,disp)
.macro AT_BP_SI_DISP8(disp) AT_IND_DISP8(AT_BP_SI,disp)
.macro AT_BP_DI_DISP8(disp) AT_IND_DISP8(AT_BP_DI,disp)
.macro AT_SI_DISP8(disp)    AT_IND_DISP8(AT_SI,disp)
.macro AT_DI_DISP8(disp)    AT_IND_DISP8(AT_DI,disp)
.macro AT_BP_DISP8(disp)    AT_IND_DISP8( (0x60000+ MEM_TAG) ,disp)
.macro AT_BX_DISP8(disp)    AT_IND_DISP8(AT_BX,disp)

.macro AT_IND_DISP16(regs,disp) \
  (regs + MOD_DISP16 + disp)

.macro AT_BX_SI_DISP16(disp) AT_IND_DISP16(AT_BX_SI,disp)
.macro AT_BX_DI_DISP16(disp) AT_IND_DISP16(AT_BX_DI,disp)
.macro AT_BP_SI_DISP16(disp) AT_IND_DISP16(AT_BP_SI,disp)
.macro AT_BP_DI_DISP16(disp) AT_IND_DISP16(AT_BP_DI,disp)
.macro AT_SI_DISP16(disp)    AT_IND_DISP16(AT_SI,disp)
.macro AT_DI_DISP16(disp)    AT_IND_DISP16(AT_DI,disp)
.macro AT_BP_DISP16(disp)    AT_IND_DISP16( (0x60000+ MEM_TAG) ,disp)
.macro AT_BX_DISP16(disp)    AT_IND_DISP16(AT_BX,disp)

.macro X86INC_CHECK_DISP16(x)                     \
  .if (x) < 65536 X86INC_CHK16_OK                 \
  .error "Word displacement 0x%0X out of range",x \
  .metalabel X86INC_CHK16_OK

.macro X86INC_CHECK_DISP8(x)                      \
  .if (x) < 256 X86INC_CHK8_OK                    \
  .error "Byte displacement 0x%0X out of range",x \
  .metalabel X86INC_CHK8_OK

.macro GET_RM_TAG(x)    (((x) >> 16) & 0xFFFF)
.macro GET_RM_DISP8(x)  ((x) & 0xFF)
.macro GET_RM_DISP16(x) ((x) & 0xFFFF)

.macro ENCODE_SINGLE_DISP(x)              \
 .if IS_DISP16(x)  X86INC_ENC_DISP16      \
 .if IS_DIR16(x)   X86INC_ENC_DISP16      \
 .if IS_DISP8(x)   X86INC_ENC_DISP8       \
 .if 1 X86INC_ENC_DONE                    \
 .metalabel X86INC_ENC_DISP16             \
  X86INC_CHECK_DISP16(GET_RM_DISP16(x))   \
  WORD( GET_RM_DISP16(x) )                \
 .if 1 X86INC_ENC_DONE                    \
 .metalabel X86INC_ENC_DISP8              \
  X86INC_CHECK_DISP8(GET_RM_DISP8(x))     \
  GET_RM_DISP8(x)                         \
 .metalabel X86INC_ENC_DONE

.macro ENCODE_DISP(src,dest) \
  ENCODE_SINGLE_DISP(src)    \
  ENCODE_SINGLE_DISP(dest)


;----------------------------------------------------------------------------
; Instruction encoding macros.
;----------------------------------------------------------------------------

;----------------------------------------------------------------------------
;                                     MOV
;----------------------------------------------------------------------------

; rem: opcode for SREG<>RM is 0x8C (or 0x8E) but MOV_OP_MODRM will add one
; for 16 bits operands, that's why 0x8B is passed instead.

.macro PRINT(x) .print "-- %0X --\n",x

.macro MOV(dest,src) \
  .if ( IS_UNKNOWN(src) .or IS_UNKNOWN(dest) ) X86INC_MOV_UNKNOWN \
  .if GET_SIZE(dest) == SIZE_32 X86INC_MOV_SIZE_32 \
  .if GET_SIZE(dest) == GET_SIZE(src) X86INC_MOV_SIZE_OK \
  .if !IS_REG(src) X86INC_MOV_SIZE_OK  \
  .if !IS_REG(dest) X86INC_MOV_SIZE_OK \
  .error "The two registers must have the same size" \
  .metalabel X86INC_MOV_SIZE_OK \
  .if (IS_IM8(src) .and IS_REG(dest)) X86INC_MOV_REG_IM8 \
  .if (IS_IM8(src) .and IS_MEM(dest)) X86INC_MOV_MEM_IM8 \
  .if (IS_IM16(src) .and IS_REG(dest)) X86INC_MOV_REG_IM16 \
  .if (IS_IM16(src) .and IS_MEM(dest)) X86INC_MOV_MEM_IM16 \
  .if 1 X86INC_MOV_NOT_IM \
  .metalabel X86INC_MOV_REG_IM8 \
  0xB0 + GET_REG(dest) GET_IM8(src) \
  .if ( GET_SIZE(dest) == SIZE_8 ) X86INC_MOV_DONE \
  .error "Source operand size (8-bit) does not fit destination" \
  .metalabel X86INC_MOV_MEM_IM8 \
  0xC6 MOD_RM(0,dest) ENCODE_SINGLE_DISP(dest) GET_IM8(src) \
  .if 1 X86INC_MOV_DONE \
  .metalabel X86INC_MOV_REG_IM16 \
  0xB8 + GET_REG(dest) WORD(GET_IM16(src)) \
  .if ( GET_SIZE(dest) == SIZE_16 ) X86INC_MOV_DONE \
  .error "Source operand size (16-bit) does not fit destination" \
  .metalabel X86INC_MOV_MEM_IM16 \
  0xC7 MOD_RM(0,dest) ENCODE_SINGLE_DISP(dest) WORD(GET_IM16(src)) \
  .if 1 X86INC_MOV_DONE \
  .metalabel X86INC_MOV_NOT_IM \
  MOV_OP_MODRM( (IS_SREG(dest) .or IS_SREG(src))*0x8B+ \
                ((~IS_SREG(dest) .and ~IS_SREG(src)) .and 1)*0x88, dest, src) \
  .if (IS_SREG(dest) & IS_SREG(src)) X86INC_MOV_ERROR_S2S \
  .if (IS_MEM(dest)  & IS_MEM(src))  X86INC_MOV_ERROR_M2M \
  .if 1 X86INC_MOV_DONE \
  .metalabel X86INC_MOV_ERROR_M2M \
  .error "Illegal move from memory to memory" \
  .metalabel X86INC_MOV_UNKNOWN \
  .error "Operand unknown (missing 'IM8()' or 'IM16()' ?)" \
  .metalabel X86INC_MOV_ERROR_S2S \
  .error "Illegal move from segment register to segment register" \
  .metalabel X86INC_MOV_SIZE_32 \
  .if IS_REG(src) .or IS_MEM(src) X86INC_MOV_SIZE_32_RM_TO_REG \
  0x66 0xB8 + GET_REG(dest) WORD(GET_IM16(src)) WORD(GET_IM16(src >> 16)) \
  .if 1 X86INC_MOV_DONE \
  .metalabel X86INC_MOV_SIZE_32_RM_TO_REG \
  .error "The encoding for this instruction is not supported yet" \ ???? MOV_OP_MODRM( 0x88, dest, src ) \
  .metalabel X86INC_MOV_DONE

.macro MOV_RM_IM8(rm,im)  0xC6 MOD_RM(0,rm) ENCODE_SINGLE_DISP(rm) im

.macro MOV_RM_IM16(rm,im) 0xC7 MOD_RM(0,rm) ENCODE_SINGLE_DISP(rm) WORD(im)

.macro MOV_AL_MEM(disp) 0xA0 WORD(disp)

.macro MOV_MEM_AL(disp) 0xA2 WORD(disp)

.macro MOV_AX_MEM(disp) 0xA1 WORD(disp)

.macro MOV_MEM_AX(disp) 0xA3 WORD(disp)


.macro XCHG(a,b) \
  .if !IS_REG(a) X86INC_XCHG_NO_REG \
  .if IS_REG(b) X86INC_XCHG_REG_REG \
  .if !IS_MEM(b) X86INC_XCHG_WRONG_OP \
  SIZE_OP_MODRM(0x86,a,b) \
  .if 1 X86INC_XCHG_DONE \
  .metalabel X86INC_XCHG_REG_REG \
  .if GET_SIZE(a) == GET_SIZE(b) X86INC_XCHG_REGS_OK \
  .error "Size mismatch" \
  .metalabel X86INC_XCHG_REGS_OK \
  SIZE_OP_MODRM(0x86,a,b) \
  .if 1 X86INC_XCHG_DONE \
  .metalabel X86INC_XCHG_NO_REG \
  .error "First operand must be a register" \
  .metalabel X86INC_XCHG_WRONG_OP \
  .error "Second operand must be a register or memory" \
  .metalabel X86INC_XCHG_DONE \

.macro XCHG_AX_REG(reg) 0x90+reg    \ only for testing this form, be careful...


;----------------------------------------------------------------------------
;                           ARITHMETIC AND LOGIC
;----------------------------------------------------------------------------

.macro GENERIC(modrmop,imrmop,dest,src) \
  .if GET_SIZE(dest) == GET_SIZE(src) X86INC_GEN_SIZE_OK \
  .if !IS_REG(src) X86INC_GEN_SIZE_OK  \
  .if !IS_REG(dest) X86INC_GEN_SIZE_OK \
  .error "The two registers must have the same size" \
  .metalabel X86INC_GEN_SIZE_OK \
  .if (IS_IM8(src)) X86INC_GEN_MEM_IM8 \
  .if (IS_IM16(src)) X86INC_GEN_MEM_IM16 \
  .if 1 X86INC_GEN_NOT_IM \
  .metalabel X86INC_GEN_MEM_IM8 \
  0x80 MOD_RM(MAKE_REG8(imrmop),dest) ENCODE_SINGLE_DISP(dest) GET_IM8(src) \
  .if 1 X86INC_GEN_DONE \
  .metalabel X86INC_GEN_MEM_IM16 \
  0x81 MOD_RM(MAKE_REG16(imrmop),dest) ENCODE_SINGLE_DISP(dest) WORD(GET_IM16(src)) \
  .if 1 X86INC_GEN_DONE \
  .metalabel X86INC_GEN_NOT_IM \
  SIZE_DIR_OP_MODRM( modrmop, dest, src ) \
  .if (IS_MEM(dest)  .and  IS_MEM(src))  X86INC_GEN_ERROR_M2M \
  .if 1 X86INC_GEN_DONE \
  .metalabel X86INC_GEN_ERROR_M2M \
  .error "Illegal move from memory to memory" \
  .metalabel X86INC_GEN_DONE


.macro ADD(dest,src) GENERIC(0x00,0,dest,src)
.macro OR(dest,src)  GENERIC(0x08,1,dest,src)
.macro ADC(dest,src) GENERIC(0x10,2,dest,src)
.macro SBB(dest,src) GENERIC(0x18,3,dest,src)
.macro AND(dest,src) GENERIC(0x20,4,dest,src)
.macro SUB(dest,src) GENERIC(0x28,5,dest,src)
.macro XOR(dest,src) GENERIC(0x30,6,dest,src)
.macro CMP(dest,src) GENERIC(0x38,7,dest,src)


.macro ADD_AL_IM8(im) 4 im            ; continue or make GENERIC() smarter...
.macro ADD_AX_IM16(im) 5 WORD(im)
.macro ADC_AL(im) 0x14 im
.macro ADC_AX(im) 0x15 WORD(im)
.macro SUB_AL(im) 0x2C im
.macro SUB_AX(im) 0x2D WORD(im)
.macro SBB_AL(im) 0x1C im
.macro SBB_AX(im) 0x1D WORD(im)
.macro AND_AL(im) 0x24 im
.macro AND_AX(im) 0x25 WORD(im)
.macro OR_AL(im) 12 im
.macro OR_AX(im) 13 WORD(im)
.macro XOR_AL(im) 0x34 im
.macro XOR_AX(im) 0x35 WORD(im)
.macro CMP_AL(im) 0x3C im
.macro CMP_AX(im) 0x3D WORD(im)


.macro AAA() 0x37
.macro DAA() 0x27
.macro AAS() 0x3F
.macro DAS() 0x2F
.macro AAM() 0xD4 10
.macro AAD() 0xD5 10
.macro CBW() 0x98
.macro CWD() 0x99


.macro UGENERIC(modrmop,imrmop,dest) \
  .if IS_REG(dest) .and ( GET_SIZE(dest) == GET_SIZE(SIZE_16) ) X86INC_UGEN_REG \
  .if GET_SIZE(dest) == SIZE_16 X86INC_UGEN_RM16 \
  .if GET_SIZE(dest) == SIZE_32 X86INC_UGEN_RM32 \
  .if GET_SIZE(dest) == SIZE_8 X86INC_UGEN_RM8 \
  .error "Unknown operand size" \
  .metalabel X86INC_UGEN_RM8 \
  0xFE MOD_RM(MAKE_REG8(imrmop),dest) ENCODE_SINGLE_DISP(dest) \
  .if 1 X86INC_UGEN_DONE \
  .metalabel X86INC_UGEN_RM32 \
  0x66 \                          size prefix -> 32 bits
  .metalabel X86INC_UGEN_RM16 \
  0xFF MOD_RM(MAKE_REG16(imrmop),dest) ENCODE_SINGLE_DISP(dest) \
  .if 1 X86INC_UGEN_DONE \
  .metalabel X86INC_UGEN_REG \
  modrmop + GET_REG(dest)  \
  .metalabel X86INC_UGEN_DONE


.macro INC(x) UGENERIC(0x40,0,x)
.macro DEC(x) UGENERIC(0x48,1,x)

.macro GRP3_GENERIC(baseop,imrmop,dest) \
  .if GET_SIZE(dest) == SIZE_16 X86INC_UGEN_RM16 \
  .if GET_SIZE(dest) == SIZE_32 X86INC_UGEN_RM32 \
  .if GET_SIZE(dest) == SIZE_8 X86INC_UGEN_RM8 \
  .error "Unknown operand size (missing SIZE8 or SIZE16?)" \
  .metalabel X86INC_UGEN_RM8 \
  baseop MOD_RM(MAKE_REG8(imrmop),dest) ENCODE_SINGLE_DISP(dest) \
  .if 1 X86INC_UGEN_DONE \
  .metalabel X86INC_UGEN_RM32 \
  0x66 \                          size prefix -> 32 bits
  .metalabel X86INC_UGEN_RM16 \
  (baseop+1) MOD_RM(MAKE_REG16(imrmop),dest) ENCODE_SINGLE_DISP(dest) \
  .metalabel X86INC_UGEN_DONE


.macro TEST(opa,opb) \
  .if ( IS_UNKNOWN(opa) .or IS_UNKNOWN(opb) ) X86INC_TEST_UNKNOWN \
  .if IS_IM8(opb) X86INC_TEST_IM8   \
  .if IS_IM16(opb) X86INC_TEST_IM16 \
  0x84 + (GET_SIZE(opa)==GET_SIZE(SIZE_16)) MOD_RM(opa,opb) \
  \                                      Must still add the smart form using AX or AL...
  ENCODE_SINGLE_DISP(opb)           \
  .if 1 X86INC_TEST_DONE            \
  .metalabel X86INC_TEST_IM8        \
  GRP3_GENERIC(0xf6,0,(opa .or SIZE_8)) GET_IM8(opb) \
  .if 1 X86INC_TEST_DONE            \
  .metalabel X86INC_TEST_IM16       \
  GRP3_GENERIC(0xf6,0,(opa .or SIZE_16)) WORD(GET_IM16(opb)) \
  .if 1 X86INC_TEST_DONE            \
  .metalabel X86INC_TEST_UNKNOWN \
  .error "Operand unknown (missing 'IM8()' or 'IM16()' ?)" \
  .metalabel X86INC_TEST_DONE

.macro TEST_AL(v) 0xa8 v
.macro TEST_AX(v) 0xa9 data16(v)


.macro NOT(rm)  GRP3_GENERIC(0xf6,2,rm)
.macro NEG(rm)  GRP3_GENERIC(0xf6,3,rm)
.macro MUL(rm)  GRP3_GENERIC(0xf6,4,rm)
.macro IMUL(rm) GRP3_GENERIC(0xf6,5,rm)
.macro DIV(rm)  GRP3_GENERIC(0xf6,6,rm)
.macro IDIV(rm) GRP3_GENERIC(0xf6,7,rm)

;----------------------------------------------------------------------------
;                                    JUMPS
;----------------------------------------------------------------------------

.macro _X86_sjmp(target,op) op target-.-1 \
  .if (target-.-1) & 0xFFFFFF80 X86INC_SJMP_NEG \
  .if 1 X86INC_SJMP_DONE \
  .metalabel X86INC_SJMP_NEG \
  .if ((target-.-1) & 0xFFFFFF80) == 0xFFFFFF80 X86INC_SJMP_DONE \
  .error "Jump target out of range" \
  .metalabel X86INC_SJMP_DONE

; Short jumps:

.macro JMP(target) _X86_sjmp(target,0xeb)
.macro JE(target) _X86_sjmp(target,0x74)
.macro JZ(target) _X86_sjmp(target,0x74)
.macro JL(target) _X86_sjmp(target,0x7C)
.macro JNGE(target) _X86_sjmp(target,0x7C)
.macro JLE(target) _X86_sjmp(target,0x7E)
.macro JNG(target) _X86_sjmp(target,0x7E)
.macro JB(target) _X86_sjmp(target,0x72)
.macro JC(target) _X86_sjmp(target,0x72)
.macro JNAE(target) _X86_sjmp(target,0x72)
.macro JBE(target) _X86_sjmp(target,0x76)
.macro JNA(target) _X86_sjmp(target,0x76)
.macro JP(target) _X86_sjmp(target,0x7A)
.macro JPE(target) _X86_sjmp(target,0x7A)
.macro JO(target) _X86_sjmp(target,0x70)
.macro JS(target) _X86_sjmp(target,0x78)
.macro JNE(target) _X86_sjmp(target,0x75)
.macro JNZ(target) _X86_sjmp(target,0x75)
.macro JNL(target) _X86_sjmp(target,0x7D)
.macro JGE(target) _X86_sjmp(target,0x7D)
.macro JNLE(target) _X86_sjmp(target,0x7F)
.macro JG(target) _X86_sjmp(target,0x7F)
.macro JNB(target) _X86_sjmp(target,0x73)
.macro JAE(target) _X86_sjmp(target,0x73)
.macro JNC(target) _X86_sjmp(target,0x73)
.macro JNBE(target) _X86_sjmp(target,0x77)
.macro JA(target) _X86_sjmp(target,0x77)
.macro JNP(target) _X86_sjmp(target,0x7B)
.macro JPO(target) _X86_sjmp(target,0x7B)
.macro JNO(target) _X86_sjmp(target,0x71)
.macro JNS(target) _X86_sjmp(target,0x79)
.macro LOOP(target) _X86_sjmp(target,0xE2)
.macro LOOPZ(target) _X86_sjmp(target,0xE1)
.macro LOOPE(target) _X86_sjmp(target,0xE1)
.macro LOOPNZ(target) _X86_sjmp(target,0xE0)
.macro LOOPNE(target) _X86_sjmp(target,0xE0)
.macro JCXZ(target) _X86_sjmp(target,0xE3)


; long jumps:

.macro JMP_DIR_INTER(seg,offset) 0xEA (offset) ( (offset) >>8) seg (seg>>8)
.macro JMP_SEG_OFF(seg,offset) JMP_DIR_INTER(seg,offset)

.macro JMP_DIR_INTRA(target) 0xe9 target-.-2 (target-.-1)>>8
.macro JMP_IND_INTRA(rm) 0xff MOD_RM(MAKE_REG8(4),rm) ENCODE_SINGLE_DISP(rm)
.macro JMP_IND_INTER(rm) 0xff MOD_RM(MAKE_REG8(5),rm) ENCODE_SINGLE_DISP(rm)

;----------------------------------------------------------------------------
;                                   CALLS
;----------------------------------------------------------------------------

.macro CALL_DIR_INTRA(target) 0xe8 target-.-2 (target-.-1)>>8
.macro CALL(target) CALL_DIR_INTRA(target)
.macro CALL_IND_INTRA(rm) 0xff MOD_RM(MAKE_REG8(2),rm) ENCODE_SINGLE_DISP(rm)
.macro CALL_DIR_INTER(seg,offset) 0x9a offset ((offset)>>8) seg ((seg)>>8)
.macro CALL_IND_INTER(rm) 0xff MOD_RM(MAKE_REG8(3),rm) ENCODE_SINGLE_DISP(rm)

.macro RET_INTRA() 0xc3
.macro RET() RET_INTRA()
.macro RET_INTRA_N(n) 0xc2 n (n)>>8
.macro RETN(n) RET_INTRA_N(n)
.macro RET_INTER() 0xcb
.macro RET_INTER_N(n) 0xca n (n)>>8

;----------------------------------------------------------------------------
;                                  INT
;----------------------------------------------------------------------------

.macro INT(type) 0xCD type
.macro INT3() 0xcc
.macro INTO() 0xce
.macro IRET() 0xcf

;----------------------------------------------------------------------------
;                                   STACK
;----------------------------------------------------------------------------

.macro PUSH(rm) \
  .if IS_REG(rm) X86INC_PUSH_REG \
  .if IS_SREG(rm) X86INC_PUSH_REG \
  OP_MODRM(0xFF,MAKE_REG8(0b110),rm) \
  ENCODE_SINGLE_DISP(rm) \
  .if 1 X86INC_PUSH_DONE \
  .metalabel X86INC_PUSH_REG \
  IS_REG(rm)*(0x50+GET_REG(rm))+IS_SREG(rm)*(6+GET_REG(rm)*8) \
  .metalabel X86INC_PUSH_DONE

.macro POP(rm) \
  .if IS_REG(rm) X86INC_POP_REG \
  .if IS_SREG(rm) X86INC_POP_REG \
  OP_MODRM(0x8F,MAKE_REG8(0),rm) \
  ENCODE_SINGLE_DISP(rm) \
  .if 1 X86INC_POP_DONE \
  .metalabel X86INC_POP_REG \
  IS_REG(rm)*(0x58+GET_REG(rm))+IS_SREG(rm)*(7+GET_REG(rm)*8) \
  .metalabel X86INC_POP_DONE

.macro PUSHF() 0x9c
.macro POPF() 0x9d


;----------------------------------------------------------------------------
;                                    I/O
;----------------------------------------------------------------------------

.macro IN(port,reg) \
  .if IS_REG(port) .and (GET_REG(port) == GET_REG(DX)) X86INC_IN_DX \
  .if (GET_REG(reg) == GET_REG(AX)) .and (GET_SIZE(reg) == GET_SIZE(SIZE_16)) X86INC_IN_AX \
  .if (GET_REG(reg) == GET_REG(AL)) .and (GET_SIZE(reg) == GET_SIZE(SIZE_8)) X86INC_IN_AL \
  .error "Illegal arguments" \
  .metalabel X86INC_IN_AX \
  0xe5 (port) \
  .if 1 X86INC_IN_DONE \
  .metalabel X86INC_IN_AL \
  0xe4 (port) \
  .if 1 X86INC_IN_DONE \
  .metalabel X86INC_IN_DX \
  .if (GET_REG(reg) == GET_REG(AX)) .and (GET_SIZE(reg) == GET_SIZE(SIZE_16)) X86INC_IN_AX \
  .if (GET_REG(reg) == GET_REG(AL)) .and (GET_SIZE(reg) == GET_SIZE(SIZE_8)) X86INC_IN_AL \
  .error "Illegal target register" \
  .metalabel X86INC_IN_AX \
  0xed \
  .if 1 X86INC_IN_DONE \
  .metalabel X86INC_IN_AL \
  0xec \
  .if 1 X86INC_IN_DONE \
  .metalabel X86INC_IN_DONE

.macro OUT(port,reg) \
  .if IS_REG(port) .and (GET_REG(port) == GET_REG(DX)) X86INC_OUT_DX \
  .if (GET_REG(reg) == GET_REG(AX)) .and (GET_SIZE(reg) == GET_SIZE(SIZE_16)) X86INC_OUT_AX \
  .if (GET_REG(reg) == GET_REG(AL)) .and (GET_SIZE(reg) == GET_SIZE(SIZE_8)) X86INC_OUT_AL \
  .error "Illegal register" \
  .metalabel X86INC_OUT_AX \
  0xe7 (port) \
  .if 1 X86INC_OUT_DONE \
  .metalabel X86INC_OUT_AL \
  0xe6 (port) \
  .if 1 X86INC_OUT_DONE \
  .metalabel X86INC_OUT_DX \
  .if (GET_REG(reg) == GET_REG(AX)) .and (GET_SIZE(reg) == GET_SIZE(SIZE_16)) X86INC_OUT_AX \
  .if (GET_REG(reg) == GET_REG(AL)) .and (GET_SIZE(reg) == GET_SIZE(SIZE_8)) X86INC_OUT_AL \
  .error "Illegal target register" \
  .metalabel X86INC_OUT_AX \
  0xef \
  .if 1 X86INC_OUT_DONE \
  .metalabel X86INC_OUT_AL \
  0xee \
  .metalabel X86INC_OUT_DONE


;----------------------------------------------------------------------------
;                                 VARIOUS
;----------------------------------------------------------------------------

.macro SAHF() 0x9E
.macro LAHF() 0x9F

.macro CLC() 0xF8
.macro CMC() 0xF5
.macro STC() 0xF9
.macro CLD() 0xFC
.macro STD() 0xFD
.macro CLI() 0xFA
.macro STI() 0xFB
.macro HLT() 0xF4

.macro XLAT() 0xd7

.macro LEA(reg,rm) 0x8d mod_rm(get_mod(rm),reg,rm)    ; obviously: those still need to be implemented properly...

.macro LDS(reg,rm) 0xc5 mod_rm(get_mod(rm),reg,rm)
.macro LES(reg,rm) 0xc4 mod_rm(get_mod(rm),reg,rm)

.macro SEG(sreg) \
  .if ( IS_SREG(sreg) != 0 ) X86INC_SEG_OK \
  .error "Operand is not a segment register" \
.metalabel X86INC_SEG_OK \
  0x26 + (GET_SREG(sreg) << 3 )

.macro WAIT() 0x9b
.macro ESC(im,rm) \
  .error "'Esc' is not implemented yet" \ 0xd8 MOD_RM(get_mod(rm),0,rm)
.macro LOCK() 0xf0

.macro SETALC() 0xD6

.macro NOP() XCHG(AX,AX)

;----------------------------------------------------------------------------
; 386+ instructions
;----------------------------------------------------------------------------

.macro OPER32() 0x66    ; turns 16-bit operations into 32-bit operations.
.macro ADDR32() 0x67    ; changes the meaning of the mod/rm byte!!!



;----------------------------------------------------------------------------
;                                STRING
;----------------------------------------------------------------------------

.macro REP() 0xF2
.macro MOVSB() 0xA4
.macro CMPSB() 0xA6
.macro SCASB() 0xAE
.macro LODSB() 0xAC
.macro STOSB() 0xAA
.macro MOVSW() 0xA5
.macro CMPSW() 0xA7
.macro SCASW() 0xAF
.macro LODSW() 0xAD
.macro STOSW() 0xAB

;----------------------------------------------------------------------------
;                                SHIFTS
;----------------------------------------------------------------------------

.macro SHIFT_GENERIC(subcode,rm,cntr)             \
  .if IS_IM8(cntr) X86_SHIFT_IM                   \
  .if IS_REG(cntr) X86_SHIFT_REG                  \
  .error "Unknown counter for shift instruction (missing IM8(n)?)"  \
  .metalabel X86_SHIFT_REG                        \
  .if GET_REG(cntr) == GET_REG(CL) X86_SHIFT_CL   \
  .error "Unknown counter register for shift instruction" \
  .metalabel X86_SHIFT_CL                         \
  OP_MODRM( (0xD2 + (GET_SIZE(rm)==SIZE_16)),MAKE_REG8(subcode),rm ) \      shift,rm,CL
  ENCODE_SINGLE_DISP(rm)                          \
  .if 1 X86_SHIFT_DONE                            \
  .metalabel X86_SHIFT_IM                         \
  .if cntr == 1 X86_SHIFT_ONE                     \
  OP_MODRM( (0xC0 + (GET_SIZE(rm)==SIZE_16)),MAKE_REG8(subcode),rm ) \      shift,rm,im8
  ENCODE_SINGLE_DISP(rm)                          \
  GET_IM8(cntr)                                   \
  .if 1 X86_SHIFT_DONE                            \
  .metalabel X86_SHIFT_ONE                        \
  OP_MODRM( (0xD0 + (GET_SIZE(rm)==SIZE_16)),MAKE_REG8(subcode),rm ) \      shift,rm,1
  ENCODE_SINGLE_DISP(rm)                          \
  .metalabel X86_SHIFT_DONE


.macro ROL(rm,count) SHIFT_GENERIC(0,rm,count)
.macro ROR(rm,count) SHIFT_GENERIC(1,rm,count)
.macro RCL(rm,count) SHIFT_GENERIC(2,rm,count)
.macro RCR(rm,count) SHIFT_GENERIC(3,rm,count)
.macro SHL(rm,count) SHIFT_GENERIC(4,rm,count)
.macro SAL(rm,count) SHIFT_GENERIC(4,rm,count)
.macro SHR(rm,count) SHIFT_GENERIC(5,rm,count)
.macro SAR(rm,count) SHIFT_GENERIC(7,rm,count)
