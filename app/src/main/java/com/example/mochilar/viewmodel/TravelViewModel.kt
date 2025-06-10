package com.example.mochilar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mochilar.data.Travel
import com.example.mochilar.data.TravelRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TravelViewModel(private val repository: TravelRepository) : ViewModel() {
    private val apiKey = "AIzaSyAPajnH4BsT95tEhI8VObMV57kasmyEINo"

    fun insertTravel(travel: Travel) {
        viewModelScope.launch {
            repository.insertTravel(travel)
        }
    }

    suspend fun getTravelsByUser(userId: Int): List<Travel> {
        return repository.getTravelsByUser(userId)
    }

    fun deleteTravel(travelId: Int) {
        viewModelScope.launch {
            repository.deleteTravel(travelId)
        }
    }

    fun updateTravel(travel: Travel) {
        viewModelScope.launch {
            repository.updateTravel(travel)
        }
    }

    suspend fun getTravelById(travelId: Int): Travel? {
        return repository.getTravelById(travelId)
    }

    suspend fun gerarRoteiroGemini(travel: Travel): String = withContext(Dispatchers.IO) {
        try {
            val generativeModel = GenerativeModel(
                modelName = "models/gemini-2.0-flash",
                apiKey = apiKey
            )

            val promptText = """
                
                Você é um assistente de viagens. Crie um roteiro diário e conciso para a viagem ao destino "${travel.destination}".
                A viagem ocorre de ${travel.startDate} a ${travel.endDate}, sendo uma viagem do tipo "${travel.travelType}".
                O orçamento total disponível é de R${'$'}${"%.2f".format(travel.budget)}.

                O roteiro deve ser prático e direto, com atividades plausíveis para os dias de viagem.

                **Importante:** A resposta deve estar completamente em **português** e **não deve conter nenhuma formatação Markdown** (ex: asteriscos, hashtags, listas com traços ou números, ou quebras desnecessárias). 
                Utilize apenas **texto simples (plain text)**. Evite símbolos de marcação ou qualquer estilização. Responda apenas com o roteiro, sem introdução ou encerramento.
                ""${'"'}
                """.trimIndent()

            val response = generativeModel.generateContent(content { text(promptText) })

            return@withContext response.text ?: "Não foi possível gerar o roteiro no momento."
        } catch (e: Exception) {
            Log.e("TravelViewModel", "Erro ao gerar roteiro", e) // Log the full error
            return@withContext "Erro ao gerar roteiro: ${e.message ?: "erro desconhecido"}"
        }
    }
}