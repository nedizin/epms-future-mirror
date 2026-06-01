package com.epms.epmssmartmirror.data

import androidx.compose.ui.graphics.Color

object AppData {
    const val SCHOOL_NAME = "Escola Profissional Mariana Seixas"
    const val APP_TITLE = "EPMS Future Mirror"
    const val SLOGAN = "Experimenta o teu futuro em 30 segundos"
    const val WEBSITE = "www.epms.pt"
    const val QR_LINK = "https://www.epms.pt/"
    const val FUTURE_MESSAGE = "O teu futuro começa na EPMS."
    const val COUNTDOWN_SECONDS = 3

    val homeMessages = listOf(
        "Experimenta o teu futuro em 30 segundos",
        "O teu futuro começa aqui",
        "Fazemos a diferença",
        "Hoje aluno. Amanhã profissional."
    )

    val profiles = listOf(
        Profile(
            name = "Técnico/a de Ação Educativa",
            headline = "Acompanha o crescimento das crianças com energia, cuidado e criatividade.",
            futureLine = "Aprende a apoiar, educar e inspirar com a EPMS.",
            accent = Color(0xFFFFC247),
            emoji = "🧸" // teddy bear
        ),
        Profile(
            name = "Técnico/a de Auxiliar de Saúde",
            headline = "Cuida de pessoas com empatia, técnica e espírito de missão.",
            futureLine = "Prepara-te para fazer a diferença na área da saúde.",
            accent = Color(0xFF7BE1C8),
            emoji = "🩺" // stethoscope
        ),
        Profile(
            name = "Cabeleireiro/a",
            headline = "Transforma estilo, imagem e confiança em experiências memoráveis.",
            futureLine = "Dá forma ao teu talento criativo com a EPMS.",
            accent = Color(0xFFFFB56B),
            emoji = "✂️" // scissors
        ),
        Profile(
            name = "Técnico/a de Cozinha e Restauração",
            headline = "Explora sabor, organização e serviço em ambientes profissionais.",
            futureLine = "Cria experiências à mesa com técnica e paixão.",
            accent = Color(0xFFF47C20),
            emoji = "👨‍🍳" // chef
        ),
        Profile(
            name = "Técnico/a de Comunicação - Marketing, Relações Públicas e Publicidade",
            headline = "Cria campanhas, marcas e mensagens com impacto real.",
            futureLine = "Comunica com estratégia, criatividade e visão de futuro.",
            accent = Color(0xFF79A9FF),
            emoji = "📣" // megaphone
        ),
        Profile(
            name = "Técnico/a de Desporto",
            headline = "Promove bem-estar, movimento e desempenho com atitude positiva.",
            futureLine = "Leva a tua energia mais longe com a EPMS.",
            accent = Color(0xFF9BE7B1),
            emoji = "⚽" // soccer ball
        ),
        Profile(
            name = "Técnico/a de Desenvolvimento de Software",
            headline = "Constrói aplicações, soluções digitais e novas experiências tecnológicas.",
            futureLine = "Programa o teu próximo passo com a EPMS.",
            accent = Color(0xFF4CE3FF),
            emoji = "💻" // laptop
        ),
        Profile(
            name = "Técnico/a de Eletrónica e Automação",
            headline = "Liga inovação, controlo e tecnologia ao mundo real.",
            futureLine = "Domina sistemas inteligentes e automação moderna.",
            accent = Color(0xFFF6A54A),
            emoji = "🤖" // robot
        ),
        Profile(
            name = "Esteticista",
            headline = "Valoriza o bem-estar, a imagem e o cuidado pessoal com criatividade.",
            futureLine = "Transforma beleza em profissão com a EPMS.",
            accent = Color(0xFFFF95C7),
            emoji = "💅" // nail polish
        ),
        Profile(
            name = "Técnico/a de Informática de Gestão",
            headline = "Une tecnologia, organização e dados para apoiar decisões.",
            futureLine = "Liga gestão e digital ao teu futuro profissional.",
            accent = Color(0xFF70B7FF),
            emoji = "📊" // bar chart
        ),
        Profile(
            name = "Técnico/a de Multimédia",
            headline = "Conta histórias com design, imagem, vídeo e criatividade digital.",
            futureLine = "Cria conteúdos que marcam com a EPMS.",
            accent = Color(0xFFB9A1FF),
            emoji = "🎬" // clapper board
        ),
        Profile(
            name = "Técnico/a de Produção de Conteúdos Interativos",
            headline = "Desenha experiências digitais envolventes para novas audiências.",
            futureLine = "Dá vida a ideias interativas com a EPMS.",
            accent = Color(0xFFFF9D73),
            emoji = "🎮" // game controller
        ),
        Profile(
            name = "Técnico/a de Sistemas de Computação e Redes",
            headline = "Garante infraestrutura, segurança e ligações que mantêm tudo a funcionar.",
            futureLine = "Constrói redes, sistemas e confiança tecnológica.",
            accent = Color(0xFF8BD3FF),
            emoji = "🌐" // globe
        )
    )
}
