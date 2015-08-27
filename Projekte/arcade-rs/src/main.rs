extern crate sdl2;
mod phi;
mod views;

use ::sdl2::pixels::Color;
use ::phi::Events;

fn main() {
	// Init SDL2
	let mut sdl_context = sdl2::init().video()
		.build().unwrap();

	// Create the window
	let window = sdl_context.window("AracadeRS Shooter", 800, 600)
		.position_centered().opengl()
		.build().unwrap();

	let mut renderer = window.renderer().accelerated()
		.build().unwrap();

	let mut events = Events::new(sdl_context.event_pump());

	'game_loop: loop {
		events.pump();

		if true == events.now.quit || Some(true) == events.now.key_escape {
			break 'game_loop;
		}

		renderer.set_draw_color(Color::RGB(0, 0, 0));
		renderer.clear();
		renderer.present();
	}
}
