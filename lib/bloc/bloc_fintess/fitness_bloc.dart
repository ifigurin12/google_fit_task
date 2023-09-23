import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

part 'fitness_event.dart';
part 'fitness_state.dart';

class FitnessBloc extends Bloc<FitnessEvent, FitnessState> {
  static const platform = MethodChannel('fitness_data');
  FitnessBloc() : super(FitnessInitial()) {
     on<FitnessCountData>(_onCountData);
  }

  FutureOr<void> _onCountData(FitnessCountData event, Emitter<FitnessState> emit) {
    emit(FitnessDataLoadingState());
    try 
    {
      final data = platform.invokeMethod('getCountData');
      print(data);
      emit(FitnessDataSuccessState(fitnessData: data));
    } catch(e) {
      emit(FitnessDataFailureState());
    }
  }
}
