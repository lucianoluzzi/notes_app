# Simple note taking app
This is just a simple note taking app, that allows you to add a title, a description, and an image URL (this was a requisite).
A dashboard will show the added notes, with a label for edited notes.
The note details screen will allow you to edit the note or delete it.

## Architecture
The project follows MVVM with Unidirectional Data Flow, as described by Google.
I've used a UseCase layer in between the ViewModel as I understand it adds additional clarity on what the app does, provides greater reusability (as compared with just Repositories), and provides greater separation of concerns.

The app is also multi-modular, with both a layer and a feature being modules (e.g. domain is a module, the note details feature is also a module).

## Testing
Testing was done using JUnit 5, as it provides test output that are easier to read, as well as the possibility to nest related test cases.
The ViewModel's and the UseCases were unit tested.

## What I would have done different
Covering the repositories with unit tests as well.
Adding UI tests (for single screens, using Espresso).
Polishing the UI a bit more (maybe adding transactions from the notes list to the details screen, adding animations for visibility changes, etc.).

On the architecture part, I decided to wrap the user actions in "intent" classes (taking from inspiration from MVI), but I don't see it adding clear benefits, so I would remove that and call the events from direct methods on the ViewModel.
