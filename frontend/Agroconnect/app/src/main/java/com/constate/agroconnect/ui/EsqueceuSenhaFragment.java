package com.constate.agroconnect.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.constate.agroconnect.R;
import com.constate.agroconnect.databinding.BottomSheetCodigoRedefinirBinding;
import com.constate.agroconnect.databinding.FragmentEsqueceuSenhaBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class EsqueceuSenhaFragment extends Fragment {
    FragmentEsqueceuSenhaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentEsqueceuSenhaBinding.inflate(inflater, container, false);
    View view = binding.getRoot();

    return view;
    }

    private void showBottonSheetDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        BottomSheetCodigoRedefinirBinding sheetBinding = BottomSheetCodigoRedefinirBinding.inflate(LayoutInflater.from(getContext()), null, false);

        sheetBinding.buttonVerificarCodigo.setOnClickListener(v -> {
            dialog.dismiss();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.carrinhoFragment);
        });

        sheetBinding.txtReenviarCodigo.setOnClickListener(v -> {
            dialog.dismiss();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_pedidos);
        });

        dialog.setContentView(sheetBinding.getRoot());
        dialog.show();
    }
}