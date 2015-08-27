use ::sdl2::render::Renderer;

// #[macro_use] asks the compiler to import the macros defined in the `events`
// module. This is necessary because macros cannot be namespaced -- macro
// expansion happens before the concept of namespace even starts to _exist_ in
// the compilation timeline.
#[macro_use] mod events;

// We cannot call functions at top-level. However, `struct_events` is not your
// usual function: it's a macro. Which means that you can use a macro to do
// pretty much anything _normal_ code would.
struct_events!(
    keyboard: {
        key_escape: Escape,
        key_up: Up,
        key_down: Down
    },
    else: {
        quit: Quit { .. }
    }
);

/// Bundles the Phi abstractions in a single structure which
/// can be passed easily between functions.
pub struct Phi<'p, 'r> {
    pub events: Events<'p>,
    pub renderer: Renderer<'r>,
}

/// A `ViewAction` is a way for the currently executed view to
/// communicate with the game loop. It specifies which action
/// should be executed before the next rendering.
pub enum ViewAction {
    None,
    Quit,
}

pub trait View {
	/// Called when 'self' becomes the main, rendered view.
	fn resume(&mut self, context: &mut Phi);

	// Called when 'self' stops being the main, rendered view.
	fn pause(&mut self, context: &mut Phi);

	/// Called on every frame to take care of both the logic and
	/// the rendering of the current view

	/// 'elapsed' is expressed in secons.
	fn render(&mut self, context: &mut Phi, elapsed: f64) -> ViewAction;
}
