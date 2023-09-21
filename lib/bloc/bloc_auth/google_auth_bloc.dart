import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

part 'google_auth_event.dart';
part 'google_auth_state.dart';

class GoogleAuthBloc extends Bloc<GoogleAuthEvent, GoogleAuthState> {
  GoogleAuthBloc() : super(GoogleAuthInitial()) {
    on<GoogleIsSignedInEvent>(_onIsSignedIn);
    on<GoogleSignInEvent>(_onSignIn);
  }

  FutureOr<void> _onSignIn(
      GoogleSignInEvent event, Emitter<GoogleAuthState> emit) async {
    emit(GoogleAuthLoadingState());
    const platform = MethodChannel('google_auth');
    try {
      bool isSignIn = await platform.invokeMethod('signIn');
      isSignIn
          ? emit(GoogleAuthSuccessState())
          : emit(GoogleAuthFailureState());
    } catch (e) {
      print(e);
    }
  }

  FutureOr<void> _onIsSignedIn(
      GoogleIsSignedInEvent event, Emitter<GoogleAuthState> emit) async {
        emit(GoogleAuthLoadingState());
    const platform = MethodChannel('google_auth');
    try {
      bool isSignedIn = await platform.invokeMethod('isSignedIn');
      isSignedIn
          ? emit(GoogleAuthSuccessState())
          : emit(GoogleAuthFailureState());
    } catch (e) {
      print(e);
    }
      }
}
