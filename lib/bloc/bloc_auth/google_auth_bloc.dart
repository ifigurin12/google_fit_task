import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

part 'google_auth_event.dart';
part 'google_auth_state.dart';

class GoogleAuthBloc extends Bloc<GoogleAuthEvent, GoogleAuthState> {
  static const platform = MethodChannel('google_auth');
  GoogleAuthBloc() : super(GoogleAuthInitial()) {
    on<GoogleIsSignedInEvent>(_onIsSignedIn);
    on<GoogleSignOutEvent>(_onSignOut);
    on<GoogleSignInEvent>(_onSignIn);
    
  }

  FutureOr<void> _onSignIn(
      GoogleSignInEvent event, Emitter<GoogleAuthState> emit) async {
    emit(GoogleAuthLoadingState());

    try {
      bool isSignIn = await platform.invokeMethod('signIn');
      isSignIn
          ? emit(GoogleAuthSuccessState())
          : emit(GoogleAuthFailureState());
    } catch (e) {
      print(e);
    }
  }

  FutureOr<void> _onSignOut(
      GoogleSignOutEvent event, Emitter<GoogleAuthState> emit) async {
    emit(GoogleAuthLoadingState());
    try {
      bool isSignOut = await platform.invokeMethod('signOut');
      isSignOut
          ? emit(GoogleAuthFailureState())
          : emit(GoogleAuthSuccessState());
    } catch (e) {
      print(e);
    }
  }

  FutureOr<void> _onIsSignedIn(
      GoogleIsSignedInEvent event, Emitter<GoogleAuthState> emit) async {
    emit(GoogleAuthLoadingState());
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
