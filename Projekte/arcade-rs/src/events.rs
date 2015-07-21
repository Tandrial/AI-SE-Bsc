macro_rules! struct_events {
    ( 
    	keyboard: { $( $k_alias:ident : $k_sdl:ident ),* },

    	// Match against a pattern
    	else: { $( $e_alias:ident : $e_sdl:pat ),* }
    ) => {
        use ::sdl2::event::EventPump;


        pub struct ImmediateEvents {
        	// For all keyboard event, we have an Option<bool>
        	// Some(true)  => Was just pressed
        	// Some(false) => Was just released
        	// None        => Nohting happening _now_
        	$( pub $k_alias: Option<bool> , )*

        	$( pub $e_alias: bool ),*
        }

        impl ImmediateEvents {
            pub fn new() -> ImmediateEvents {
                ImmediateEvents {
                	// When reinitialized, nothing has yet happend, so all are set to None
                	$( $k_alias: None , )*
                	$( $e_alias: false ),*
                }
            }
        }


        pub struct Events<'p> {
            pump: EventPump<'p>,
            pub now: ImmediateEvents,

            // true  => pressed
            // false => not pressed
            $( pub $k_alias: bool),*
        }

        impl<'p> Events<'p> {
            pub fn new(pump: EventPump<'p>) -> Events<'p> {
                Events {
                    pump: pump,
                    now: ImmediateEvents::new(),

                    // By default, initialize every key with _not pressed_
                    $( $k_alias: false ),*
                }
            }

            pub fn pump(&mut self) {
                self.now = ImmediateEvents::new();

                for event in self.pump.poll_iter() {
                    use ::sdl2::event::Event::*;
                    use ::sdl2::keyboard::Keycode::*;

                    match event {
                    	KeyDown { keycode, .. } => match keycode {
                    		$(
                    			Some($k_sdl) => {
                    				// Prevent multiple presses when keeping a key down
                    				// Was previously not pressed ?
                    				if !self.$k_alias {
                    					// Key pressed
                    					self.now.$k_alias = Some(true);
                    				}
                    				self.$k_alias = true;
                    			}
                    		),*
                    		_ => {}
                    	},

                    	KeyUp { keycode, .. } => match keycode {
                    		$(
                    			Some($k_sdl) => {
                    				// Key released
                    				self.now.$k_alias = Some(false);
                    				self.$k_alias = false;
                    			}
                    		),*
                    		_ => {}
                    	},
                    	
                    	$(
                    	 	$e_sdl => {
                    	 		self.now.$e_alias = true;
                    	 	}
                    	)*,

                        _ => {}
                    }
                }
            }
        }
    }
}
